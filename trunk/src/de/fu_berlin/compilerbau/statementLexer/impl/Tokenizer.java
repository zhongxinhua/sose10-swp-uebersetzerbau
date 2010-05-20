package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;

class Tokenizer implements Iterator<StatementNode> {
	
	protected StatementNode nextNodeToRead;
	protected PositionCharacterStream stream;
	
	Tokenizer(PositionCharacterStream stream) {
		this.stream = stream;
	}
	
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	protected StatementNode primitiveGetNext() {
		while(stream.hasNext()) {
			Character next = stream.next();
			if(!Character.isWhitespace(next)) {
				stream.pushback(next);
				break;
			}
		}
		if(!stream.hasNext()) {
			return null;
		}
		
		final int start = stream.getStart();
		final int line = stream.getLine();
		final int character = stream.getCharacter();
		
		TokenType type = null;
		final char next = stream.next();
		switch(next) {
			case '.': type = TokenType.DOT; break;
			case '(': type = TokenType.PAREN_OPEN; break;
			case ')': type = TokenType.PARENT_CLOSE; break;
			case '[': type = TokenType.BRACKET_OPEN; break;
			case ']': type = TokenType.BRACKET_CLOSE; break;
			case '*': type = TokenType.TIMES; break;
			case '/': type = TokenType.SLASH; break;
			case '%': type = TokenType.MOD; break;
			case ',': type = TokenType.COMMA; break;
		}
		if(type != null) {
			return new StatementNodeImpl(start, line, character, type, null);
		}
		
		switch(next) {
			case '-':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '-') {
						type = TokenType.MINUS;
						stream.pushback(char1);
					} else {
						type = TokenType.DECR;
					}
				}
				break;
				
			case '+':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '+') {
						type = TokenType.PLUS;
						stream.pushback(char1);
					} else {
						type = TokenType.INCR;
					}
				}
				break;
				
			case 'n': {
				if(!stream.hasNext()) break;
				final char char1 = stream.next();
				if(!stream.hasNext()) {
					stream.pushback(char1);
					break;
				}
				
				if(char1 == 'e') {
					final char char2 = stream.next();
					if(char2 != 'w') {
						stream.pushback(char2);
						stream.pushback('e');
						break;
					}
					if(!stream.hasNext()) {
						type = TokenType.NEW;
						break;
					}
					final char char3 = stream.next();
					if(Character.isLetterOrDigit(char3)) {
						stream.pushback(char3);
						stream.pushback('w');
						stream.pushback('e');
						break;
					}
					type = TokenType.NEW;
					break;
				} else if(char1 == 'u') {
					final char char2 = stream.next();
					if(char2 != 'l') {
						stream.pushback(char2);
						stream.pushback('u');
						break;
					}
					final char char3= stream.next();
					if(char3 != 'l') {
						stream.pushback(char2);
						stream.pushback('l');
						stream.pushback('u');
						break;
					}
					if(!stream.hasNext()) {
						type = TokenType.NULL;
						break;
					}
					final char char4 = stream.next();
					if(Character.isLetterOrDigit(char4)) {
						stream.pushback(char4);
						stream.pushback('l');
						stream.pushback('l');
						stream.pushback('u');
						break;
					}
					type = TokenType.NULL;
					break;
				} else {
					stream.pushback(char1);
					break;
				}
			}
			
			case '<':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '=') {
						type = TokenType.LT;
						stream.pushback(char1);
					} else {
						type = TokenType.LE;
					}
				}
				break;
				
			case '>':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '=') {
						type = TokenType.GT;
						stream.pushback(char1);
					} else {
						type = TokenType.GE;
					}
				}
				break;
				
			case '!':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '=') {
						type = TokenType.NOT;
						stream.pushback(char1);
					} else {
						type = TokenType.NE;
					}
				}
				break;
				
			case '=':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '=') {
						type = TokenType.ASSIGN;
						stream.pushback(char1);
					} else {
						type = TokenType.EQ;
					}
				}
				break;
				
			case '&':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '&') {
						type = TokenType.AND;
						stream.pushback(char1);
					} else {
						type = TokenType.BIT_AND;
					}
				}
				break;
				
			case '|':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '&') {
						type = TokenType.OR;
						stream.pushback(char1);
					} else {
						type = TokenType.BIT_OR;
					}
				}
				break;
		}
		if(type != null) {
			return new StatementNodeImpl(start, line, character, type, null);
		}

		// TODO: ID / INT / REAL / STRING
		
		return new StatementNodeImpl(start, line, character, TokenType.ERROR, null);
	}

	@Override
	public boolean hasNext() {
		if(nextNodeToRead != null) {
			return true;
		}
		nextNodeToRead = primitiveGetNext();
		return nextNodeToRead != null;
	}

	@Override
	public StatementNode next() throws NoSuchElementException {
		if(hasNext()) {
			final StatementNode result = nextNodeToRead;
			nextNodeToRead = null;
			return result;
		}
		throw new NoSuchElementException();
	}
	
}
