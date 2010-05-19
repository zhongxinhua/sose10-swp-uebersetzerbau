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
		
		/* TODO: mehr als ein Zeichen / als ein Zeichen nicht eindeutig 
			INCR,
			DECR,
			NEW,
			PLUS,
			MINUS,
			LE,
			GE,
			LT,
			GT,
			NE,
			EQ,
			BIT_AND,
			BIT_XOR,
			BIT_OR,
			AND,
			OR,
			COMMA,
		 */
		TokenType type = null;
		char next = stream.next();
		switch(next) {
			case '.': type = TokenType.DOT; break;
			case '(': type = TokenType.PAREN_OPEN; break;
			case ')': type = TokenType.PARENT_CLOSE; break;
			case '[': type = TokenType.BRACKET_OPEN; break;
			case ']': type = TokenType.BRACKET_CLOSE; break;
			case '!': type = TokenType.NOT; break;
			case '*': type = TokenType.TIMES; break;
			case '/': type = TokenType.SLASH; break;
			case '%': type = TokenType.MOD; break;
			case ',': type = TokenType.COMMA; break;
		}
		if(type != null) {
			return new StatementNodeImpl(start, line, character, type, null);
		}
		
		return null;
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
