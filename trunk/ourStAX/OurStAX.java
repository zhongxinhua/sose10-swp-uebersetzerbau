package ourStAX;

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
	
	public Iterator<INode> iterator() {
		return new NodeIterator(this);
	}
	
	public void close() throws IOException {
		reader.close();
	}
	
	@Override
	public String toString() {
		return reader.toString() + super.toString();
	}
	
	protected Node next = null;
	
	boolean hasNext() throws IOException {
		if(next == null) {
			fetchNext();
			return next != null;
		} else {
			return true;
		}
	}
	
	Node getNext() throws IOException, NoSuchElementException {
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
		ERROR, START, TEXT, OPEN, COMMENT0, CLOSE, TAG, INNER, INNER_CLOSE, ATTR
	}
	
	protected Character nextChar = null;
	protected int readNext() throws IOException {
		if(nextChar == null) {
			final int result = reader.read();
			if(result < 0) {
				new EOFException();
			}
			++this.start;
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
						pushChar((char)next);
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
	
	protected void pushChar(char c) throws IllegalStateException {
		if(nextChar == null) {
			nextChar = Character.valueOf(c);
		} else {
			throw new IllegalStateException();
		}
	}
	
	protected State state = State.START;
	/**
	 * Reads the next {@link Node} into {@link #state}.
	 * {@link #state} remains null, if EOF was reached.
	 */
	protected void fetchNext() throws IOException {
		NodeType resultType = null;
		final State initialState = state;
		StringBuffer key = null, value = null;
		
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
							this.next = new Node(start, line, character, NodeType.NT_ERROR, null, null);
							return; // ERROR
						}
						default: {
							if(c >= 0) {
								resultType = NodeType.NT_TEXT;
								state = State.TEXT;
								value = new StringBuffer(c);
							} else {
								return; // EOF
							}
						} 
					}
					break;
				}
				
				case TEXT: {
					if(c == '<' || c == '>') {
						pushChar((char)c);
					} else if(c >= 0) {
						value.append((char)c);
						continue;
					}
					state = State.START;
					this.next = new Node(start, line, character, NodeType.NT_TEXT, null, value.toString());
					return;
				}
				
				case OPEN: {
					switch(c) {
						case('!'): {
							state = State.COMMENT0;
							break;
						}
						case('/'): {
							state = State.CLOSE;
							break;
						}
						default: {
							if(c >= 0 && (c=='_' || Character.isLetter(c))) {
								this.state = State.TAG;
								resultType = NodeType.NT_TAG;
								key = new StringBuffer((char)c);
								break;
							} else {
								state = State.ERROR;
								this.next = new Node(start, line, character, NodeType.NT_ERROR, null, null);
								return;
							}
						}
					}
					break;
				}
				
				case COMMENT0: {
					// TODO
					break;
				}
				
				case CLOSE: {
					// TODO
					break;
				}
				
				case TAG: {
					switch(c) {
						case('>'): {
							state = State.START;
							this.next = new Node(start, line, character, NodeType.NT_TAG, key.toString(), null);
							return;
						}
						case('/'): {
							pushChar('/');
							state = State.INNER_CLOSE;
							this.next = new Node(start, line, character, NodeType.NT_TAG, key.toString(), null);
							return;
						}
						case('.'): case(':'): case('-'): case('_'): {
							key.append((char)c);
							break;
						}
						default: {
							if(c >= 0 && Character.isLetterOrDigit(c)) {
								key.append((char)c);
								break;
							} else {
								state = State.ERROR;
								this.next = new Node(start, line, character, NodeType.NT_ERROR, null, null);
								return;
							} 
						}
					}
					break;
				}
				
				case INNER: {
					if(c == '/') {
						state = State.INNER_CLOSE;
					} else if(c >= 0 && Character.isWhitespace(c)) {
						// NOOP
					} else if(c >= 0 && (c=='_' || Character.isLetter(c))) {
						this.state = State.ATTR;
						resultType = NodeType.NT_ATTR;
						key = new StringBuffer((char)c);
						break;
					} else {
						state = State.ERROR;
						this.next = new Node(start, line, character, NodeType.NT_ERROR, null, null);
						return;
					}
					break;
				}
				
				case INNER_CLOSE: {
					if(c == '>') {
						state = State.START;
						this.next = new Node(start, line, character, NodeType.NT_END_TAG, null, null);
						return;
					} else {
						state = State.ERROR;
						this.next = new Node(start, line, character, NodeType.NT_ERROR, null, null);
						return;
					}
				}
				
				case ATTR: {
					// TODO
					break;
				}
				
				default: {
					throw new IllegalStateException("state == " + state);
				}
			}
		}
	}
	
}
