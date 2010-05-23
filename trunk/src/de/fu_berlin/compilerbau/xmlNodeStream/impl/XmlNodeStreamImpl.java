package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.util.PositionCharacterStream;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.PositionStringBuilder;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;

import static de.fu_berlin.compilerbau.util.CharacterType.*;

/**
 * The actual class that parses the XML input.
 * @author rene
 */
class XmlNodeStreamImpl implements XmlNodeStream {
	
	private static final long serialVersionUID = 8245550611478586672L;
	
	protected final PositionCharacterStream stream;
	
	XmlNodeStreamImpl(PositionString str) throws IOException {
		stream = new PositionCharacterStream(str);
	}
	
	XmlNodeStreamImpl(Reader reader, StreamPosition pos) throws IOException {
		stream = new PositionCharacterStream(reader, pos);
	}
	
	@Override
	public Iterator<XmlNode> iterator() {
		return new XmlNodeIterator(this);
	}
	
	@Override
	public void close() throws IOException {
		stream.close();
	}

	@Override
	public int getCharacter() {
		return stream.getCharacter();
	}

	@Override
	public int getLine() {
		return stream.getLine();
	}

	@Override
	public int getStart() {
		return stream.getStart();
	}
	
	@Override
	public String toString() {
		return stream.toString();
	}
	
	/**
	 * Next Node to be returned by {@link #getNext()}.
	 */
	protected XmlNodeImpl next = null;
	
	/**
	 * Can a {@link XmlNodeImpl} be read by {@link #getNext()}?
	 * 
	 * @return true if a {@link XmlNodeImpl} can be read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws IllegalStateException already returned an {@link XmlNodeImpl} indication an error.
	 */
	boolean hasNext() throws IOException, IllegalStateException {
		if(next != null) {
			return true; // a cached Node
		} else if(state == State.ERROR) {
			return false; // there cannot be any Nodes to read, once an ERROR was detected
		} else {
			next = fetchNext();
			return next != null; // the ERROR should be propagated once
		}
	}
	
	/**
	 * Retrieves next read XML node.
	 * 
	 * @return XML node read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws NoSuchElementException {@link #hasNext()} returned false
	 * @throws IllegalStateException already returned an {@link XmlNodeImpl} indication an error.
	 */
	XmlNodeImpl getNext() throws IOException, NoSuchElementException, IllegalStateException {
		if(hasNext()) {
			XmlNodeImpl next = this.next;
			this.next = null;
			return next;
		} else { // the contract of Java's iterators was broken!
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
	 * Current State
	 */
	protected State state = State.START;

	/**
	 * Reads the next {@link XmlNodeImpl} into {@link #state}. {@link #state} remains null, if EOF was
	 * reached.
	 * 
	 * @return Node read
	 * @throws IOException The underlying {@link #reader} threw an Exception.
	 * @throws IllegalStateException {@link #state} was ERROR.
	 */
	protected XmlNodeImpl fetchNext() throws IOException, IllegalStateException {
		PositionStringBuilder key = null, value = null;
		
		final int start = stream.getStart();
		final int line = stream.getLine();
		final int character = stream.getCharacter();
		
		for(;;) {
			final int c;
			if(stream.hasNext()) {
				c = stream.next();
			} else {
				c = -1;
			}
			switch(state) { // a simple state machine, not employing a bitmap as Igor did not want any ;)
				case START: {
					switch(c) {
						case '<': {
							state = State.OPEN;
							break;
						}
						case '>': {
							state = State.ERROR;
							return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
						}
						default: {
							if(c >= 0) {
								state = State.TEXT;
								value = new PositionStringBuilder(stream);
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
						stream.pushback((char)c);
					} else if(c >= 0) {
						value.append((char)c);
						continue;
					}
					state = State.START;
					return new XmlNodeImpl(start, line, character, NodeType.NT_TEXT, null, value.toPositionString());
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
							if(c >= 0 && isValidFirstLeterForTag((char)c)) {
								this.state = State.TAG;
								key = new PositionStringBuilder(stream);
								key.append((char)c);
								break;
							} else {
								state = State.ERROR;
								return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
							return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
						}
					}
					break;
				}
				
				case COMMENT1: {
					if(c == '-') {
						state = State.COMMENT;
						value = new PositionStringBuilder(stream);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case COMMENT3: {
					if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_COMMENT, null, value.toPositionString());
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA1: {
					if(c == 'C' && stream.next() == 'D' && stream.next() == 'A' && stream.next() == 'T' && stream.next() == 'A' && stream.next() == '[') {
						// I did not want to make it 6 states ...
						state = State.CDATA;
						value = new PositionStringBuilder(stream);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CDATA3: {
					if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_TEXT, null, value.toPositionString());
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE0: {
					if(c >= 0 && isValidFirstLeterForTag((char)c)) {
						this.state = State.CLOSE;
						key = new PositionStringBuilder(stream);
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE: {
					if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_END_TAG, key.toPositionString(), null);
					} else if(c >= 0 && isValidSecondLeterForTag((char)c)) {
						key.append((char)c);
						break;
					} else if(isWhitespace((char)c)) {
						state = State.CLOSE1;
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case CLOSE1: {
					if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_END_TAG, key.toPositionString(), null);
					} else if(isWhitespace((char)c)) {
						break; // NOOP;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case TAG: {
					switch(c) {
						case('>'): {
							state = State.START;
							return new XmlNodeImpl(start, line, character, NodeType.NT_TAG, key.toPositionString(), null);
						}
						case('/'): {
							stream.pushback('/');
							state = State.INNER;
							return new XmlNodeImpl(start, line, character, NodeType.NT_TAG, key.toPositionString(), null);
						}
						default: {
							if(c >= 0 && isValidSecondLeterForTag((char)c)) {
								key.append((char)c);
								break;
							} else if(c >= 0 && isWhitespace((char)c)){
								state = State.INNER;
								return new XmlNodeImpl(start, line, character, NodeType.NT_TAG, key.toPositionString(), null);
							} else {
								state = State.ERROR;
								return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
					} else if(c >= 0 && isWhitespace((char)c)) {
						break; // NOOP
					} else if(c >= 0 && isValidFirstLeterForTag((char)c)) {
						this.state = State.ATTR;
						key = new PositionStringBuilder(stream);
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case INNER_CLOSE: {
					if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_END_TAG, null, null);
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR: {
					if(c == '=') {
						state = State.ATTR1;
						value = new PositionStringBuilder(stream);
						break;
					} else if(c >= 0 && isValidSecondLeterForTag((char)c)) {
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case ATTR2: {
					if(c == '/') {
						state = State.INNER;
						stream.pushback('/');
						return new XmlNodeImpl(start, line, character, NodeType.NT_ATTR, key.toPositionString(), value.toPositionString());
					} else if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ATTR, key.toPositionString(), value.toPositionString());
					} else if(c >= 0 && isWhitespace((char)c)) {
						state = State.INNER;
						if(c == '/') {
							stream.pushback('/');
						}
						return new XmlNodeImpl(start, line, character, NodeType.NT_ATTR, key.toPositionString(), value.toPositionString());
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_TARGET0: {
					if(c >= 0 && isValidFirstLeterForTag((char)c)) {
						this.state = State.PI_TARGET;
						key = new PositionStringBuilder(stream);
						key.append((char)c);
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_TARGET: {
					if(c == '?') {
						if(stream.next() == '>') {
							state = State.START;
							return new XmlNodeImpl(start, line, character, NodeType.NT_PI, key.toPositionString(), null);
						} else {
							state = State.ERROR;
							return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
						}
					} else if(c >= 0 && isValidSecondLeterForTag((char)c)) {
						key.append((char)c);
						break;
					} else if(c >= 0 && isWhitespace((char)c)) {
						value = new PositionStringBuilder(stream);
						state = State.PI_INNER;
						break;
					} else {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					}
				}
				
				case PI_INNER: {
					if(c < 0) {
						state = State.ERROR;
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
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
						return new XmlNodeImpl(start, line, character, NodeType.NT_ERROR, null, null);
					} else if(c == '>') {
						state = State.START;
						return new XmlNodeImpl(start, line, character, NodeType.NT_PI, key.toPositionString(), value.toPositionString());
					} else {
						value.append('?');
						value.append((char)c);
						break;
					}
				}
				
				default: { // holds for ERROR, too (once an ERROR was detected, fetchNext() should not be called again)
					throw new IllegalStateException("(Internal) state == " + state);
				}
			}
		}
	}
	
}
