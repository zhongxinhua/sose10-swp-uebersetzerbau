package de.fu_berlin.compilerbau.ourStAX;

import java.io.*;
import java.util.*;

class OurStAX extends Position implements IOurStAX {
	
	protected final BufferedReader reader;
	
	OurStAX(Reader reader) throws IOException {
		super(1,1,1);
		if(reader instanceof BufferedReader) {
			this.reader = (BufferedReader)reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
	}
	
	@Override
	public Iterator<INode> iterator() {
		return new NodeIterator(this);
	}
	
	@Override
	public void close() throws IOException {
		reader.close();
	}
	
	@Override
	public String toString() {
		return reader.toString() + super.toString();
	}
	
	/**
	 * Next Node to be returned by {@link #getNext()}.
	 */
	protected Node next = null;
	
	/**
	 * Can a {@link Node} be read by {@link #getNext()}?
	 * 
	 * @return true if a {@link Node} can be read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws IllegalStateException already returned an {@link Node} indication an error.
	 */
	boolean hasNext() throws IOException, IllegalStateException {
		if(next != null) {
			return true;
		} else if(state == State.ERROR) {
			return false;
		} else {
			next = fetchNext();
			return next != null;
		}
	}
	
	/**
	 * Retrieves next read XML node.
	 * 
	 * @return XML node read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws NoSuchElementException {@link #hasNext()} returned false
	 * @throws IllegalStateException already returned an {@link Node} indication an error.
	 */
	Node getNext() throws IOException, NoSuchElementException, IllegalStateException {
		if(hasNext()) {
			Node next = this.next;
			this.next = null;
			return next;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// ACTUAL IMPLEMENTATION
	/////////////////////////////////////////////////////////////////////////////
	
	protected enum State {
		ERROR,
		
		START, TEXT, OPEN, COMMENT0, TAG, INNER, INNER_CLOSE, ATTR,
		ATTR1, ATTR_APOS, ATTR_QUOT, ATTR2, CLOSE0, CLOSE, CLOSE1, COMMENT1,
		COMMENT, COMMENT2, COMMENT3, CDATA1, CDATA, CDATA2, CDATA3, PI_TARGET0,
		PI_TARGET,PI_INNER, PI_INNER1
	}
	
	/**
	 * one character lookahead ...
	 */
	protected Character nextChar = null;
	
	/**
	 * Returns and purges {@link #nextChar} or reads next character from {@link #reader}, if nextChar
	 * was empty.
	 * 
	 * @return character read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 */
	protected int readNext() throws IOException {
		if(nextChar == null) {
			final int result = reader.read();
			if(result < 0) {
				new EOFException();
			}
			++this.start;
			++this.character;
			if(result == '\n') {
				this.character = 1;
				++this.line;
				return '\n';
			} else if(result == '\r') {
				this.character = 1;
				++this.line;
				final int next = reader.read();
				if(next >= 0) {
					++this.start;
					if(next != '\n') {
						pushCharCharacter((char)next);
					}
				}
				return '\n';
			} else {
				return result;
			}
		} else {
			final char result = nextChar.charValue();
			nextChar = null;
			return result;
		}
	}
	
	/**
	 * Pushes a character to be read by {@link #fetchNext()}.
	 * 
	 * @param c character to push
	 * @throws IllegalStateException there can only be one character in chain
	 */
	protected void pushCharCharacter(char c) throws IllegalStateException {
		if(nextChar == null) {
			nextChar = Character.valueOf(c);
		} else {
			throw new IllegalStateException("(Internal) lookahead already fed");
		}
	}
	
	/**
	 * Current State
	 */
	protected State state = State.START;

	/**
	 * Reads the next {@link Node} into {@link #state}. {@link #state} remains null, if EOF was
	 * reached.
	 * 
	 * @return Node read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws IllegalStateException {@link #state} was ERROR.
	 */
	protected Node fetchNext() throws IOException, IllegalStateException {
		Appendable key = null, value = null;
		
		final int start = this.start, line = this.line, character = this.character;
		
		for(;;) {
			final int c = readNext();
			switch(state) {
				case START: {
					switch(c) {
						case '<': {
							state = State.OPEN;
							break;
						}
						case '>': {
							state = State.ERROR;
							return new Node(start, line, character, NodeType.NT_ERROR, null, null);
						}
						default: {
							if(c >= 0) {
								state = State.TEXT;
								value = new StringBuilder();
								value.append((char)c);
							} else {
								return null; // EOF
							}
						} 
					}
					break;
				}
				
				case TEXT: {
					if(c == '<' || c == '>') {
						pushCharCharacter((char)c);
					} else if(c >= 0) {
						value.append((char)c);
						continue;
					}
					state = State.START;
					return new Node(start, line, character, NodeType.NT_TEXT, null, value.toString());
				}
				
				case OPEN: {
					switch(c) {
						case('!'): {
							state = State.COMMENT0;
							break;
						}
						case('/'): {
							state = State.CLOSE0;
							break;
						}
						case('?'): {
							state = State.PI_TARGET0;
							break;
						}
						default: {
							if(c >= 0 && (c=='_' || Character.isLetter(c))) {
								this.state = State.TAG;
								key = new StringBuilder();
								key.append((char)c);
								break;
							} else {
								state = State.ERROR;
								return new Node(start, line, character, NodeType.NT_ERROR, null, null);
							}
						}
					}
					break;
				}
				
				case COMMENT0: {
					switch(c) {
						case('['): {
							state = State.CDATA1;
							break;
						}
						case('-'): {
							state = State.COMMENT1;
							break;
						}
						default: {
							state = State.ERROR;
							return new Node(start, line, character, NodeType.NT_ERROR, null, null);
						}
					}
					break;
				}
				
				case COMMENT1: {
					if(c == '-') {
						state = State.COMMENT;
						value = new StringBuilder();
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case COMMENT: {
					if(c == '-') {
						state = State.COMMENT2;
						break;
					} else if(c >= 0) {
						value.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case COMMENT2: {
					if(c == '-') {
						state = State.COMMENT3;
						break;
					} else if(c >= 0) {
						value.append('-');
						value.append((char)c);
						state = State.COMMENT;
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case COMMENT3: {
					if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_COMMENT, null, value.toString());
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA1: {
					if(c == 'C' && readNext() == 'D' && readNext() == 'A' && readNext() == 'T' && readNext() == 'A' && readNext() == '[') {
						state = State.CDATA;
						value = new StringBuilder();
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA: {
					if(c == ']') {
						state = State.CDATA2;
						break;
					} else if(c >= 0) {
						value.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA2: {
					if(c == ']') {
						state = State.CDATA3;
						break;
					} else if(c >= 0) {
						value.append(']');
						value.append((char)c);
						state = State.CDATA;
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA3: {
					if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_TEXT, null, value.toString());
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE0: {
					if(c >= 0 && (c=='_' || Character.isLetter(c))) {
						this.state = State.CLOSE;
						key = new StringBuilder();
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE: {
					if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_END_TAG, key.toString(), null);
					} else if(c >= 0 && (c=='.' || c==':' || c=='-' || c=='_' || Character.isLetterOrDigit(c))) {
						key.append((char)c);
						break;
					} else if(Character.isWhitespace(c)) {
						state = State.CLOSE1;
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE1: {
					if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_END_TAG, key.toString(), null);
					} else if(Character.isWhitespace(c)) {
						break; // NOOP;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case TAG: {
					switch(c) {
						case('>'): {
							state = State.START;
							return new Node(start, line, character, NodeType.NT_TAG, key.toString(), null);
						}
						case('/'): {
							pushCharCharacter('/');
							state = State.INNER;
							return new Node(start, line, character, NodeType.NT_TAG, key.toString(), null);
						}
						case('.'): case(':'): case('-'): case('_'): {
							key.append((char)c);
							break;
						}
						default: {
							if(c >= 0 && Character.isLetterOrDigit(c)) {
								key.append((char)c);
								break;
							} else if(c >= 0 && Character.isWhitespace(c)){
								state = State.INNER;
								return new Node(start, line, character, NodeType.NT_TAG, key.toString(), null);
							} else {
								state = State.ERROR;
								return new Node(start, line, character, NodeType.NT_ERROR, null, null);
							} 
						}
					}
					break;
				}
				
				case INNER: {
					if(c == '/') {
						state = State.INNER_CLOSE;
						break;
					} else if(c == '>') {
						state = State.START;
						break;
					} else if(c >= 0 && Character.isWhitespace(c)) {
						break; // NOOP
					} else if(c >= 0 && (c=='_' || Character.isLetter(c))) {
						this.state = State.ATTR;
						key = new StringBuilder();
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case INNER_CLOSE: {
					if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_END_TAG, null, null);
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR: {
					if(c == '=') {
						state = State.ATTR1;
						value = new StringBuilder();
						break;
					} else if(c >= 0 && (c=='.' || c==':' || c=='-' || c=='_' || Character.isLetterOrDigit(c))) {
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR1: {
					if(c == '"') {
						state = State.ATTR_QUOT;
						break;
					} else if(c == '\'') {
						state = State.ATTR_APOS;
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR_QUOT: {
					if(c == '"') {
						state = State.ATTR2;
						break;
					} else if(c >= 0) {
						value.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR_APOS: {
					if(c == '\'') {
						state = State.ATTR2;
						break;
					} else if(c >= 0) {
						value.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR2: {
					if(c == '/') {
						state = State.INNER;
						pushCharCharacter('/');
						return new Node(start, line, character, NodeType.NT_ATTR, key.toString(), value.toString());
					} else if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_ATTR, key.toString(), value.toString());
					} else if(c >= 0 && Character.isWhitespace(c)) {
						state = State.INNER;
						if(c == '/') {
							pushCharCharacter('/');
						}
						return new Node(start, line, character, NodeType.NT_ATTR, key.toString(), value.toString());
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_TARGET0: {
					if(c >= 0 && (c=='_' || Character.isLetter(c))) {
						this.state = State.PI_TARGET;
						key = new StringBuilder();
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_TARGET: {
					if(c == '?') {
						if(readNext() == '>') {
							state = State.START;
							return new Node(start, line, character, NodeType.NT_PI, key.toString(), "");
						} else {
							state = State.ERROR;
							return new Node(start, line, character, NodeType.NT_ERROR, null, null);
						}
					} else if(c >= 0 && Character.isLetterOrDigit(c)) {
						key.append((char)c);
						break;
					} else if(c=='.' || c==':' || c=='-' || c=='_' || (c >= 0 && Character.isWhitespace(c))) {
						value = new StringBuilder();
						state = State.PI_INNER;
						break;
					} else {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_INNER: {
					if(c < 0) {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					} else if(c == '?') {
						state = State.PI_INNER1;
						break;
					} else {
						value.append((char)c);
						break;
					}
				}
				
				case PI_INNER1: {
					if(c < 0) {
						state = State.ERROR;
						return new Node(start, line, character, NodeType.NT_ERROR, null, null);
					} else if(c == '>') {
						state = State.START;
						return new Node(start, line, character, NodeType.NT_PI, key.toString(), value.toString());
					} else {
						value.append('?');
						value.append((char)c);
						break;
					}
				}
				
				default: {
					throw new IllegalStateException("(Internal) state == " + state);
				}
			}
		}
	}
	
}
