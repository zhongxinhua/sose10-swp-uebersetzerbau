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
		int next = stream.next();
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
		
		if(type == null) {
			// TODO
			switch(next) {
				case '-':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '-') {
							type = TokenType.MINUS;
							stream.pushback((char)next);
						} else {
							type = TokenType.DECR;
						}
					}
					break;
					
				case '+':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '+') {
							type = TokenType.PLUS;
							stream.pushback((char)next);
						} else {
							type = TokenType.INCR;
						}
					}
					break;
					
				case 'n':
					break; // TODO
				
				case '<':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '=') {
							type = TokenType.LT;
							stream.pushback((char)next);
						} else {
							type = TokenType.LE;
						}
					}
					break;
					
				case '>':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '=') {
							type = TokenType.GT;
							stream.pushback((char)next);
						} else {
							type = TokenType.GE;
						}
					}
					break;
					
				case '!':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '=') {
							type = TokenType.NOT;
							stream.pushback((char)next);
						} else {
							type = TokenType.NE;
						}
					}
					break;
					
				case '=':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '=') {
							type = TokenType.ASSIGN;
							stream.pushback((char)next);
						} else {
							type = TokenType.EQ;
						}
					}
					break;
					
				case '&':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '&') {
							type = TokenType.AND;
							stream.pushback((char)next);
						} else {
							type = TokenType.BIT_AND;
						}
					}
					break;
					
				case '|':
					if(stream.hasNext()) {
						next = stream.next();
						if(next != '&') {
							type = TokenType.OR;
							stream.pushback((char)next);
						} else {
							type = TokenType.BIT_OR;
						}
					}
					break;
			}
		}

		return new StatementNodeImpl(start, line, character, type, null);
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
