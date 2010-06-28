/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.statementLexer.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;
import de.fu_berlin.compilerbau.util.PositionStringBuilder;

import static de.fu_berlin.compilerbau.util.CharacterType.*;

class StatementNodeIterator implements Iterator<StatementNode> {
	
	protected StatementNode nextNodeToRead;
	protected PositionCharacterStream stream;
	
	StatementNodeIterator(PositionCharacterStream stream) {
		this.stream = stream;
	}
	
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	protected StatementNode primitiveGetNext() throws IOException {
		while(stream.hasNext()) {
			Character next = stream.next();
			if(!isWhitespace(next)) {
				stream.pushback(next);
				break;
			}
		}
		if(!stream.hasNext()) {
			return null;
		}
		
		final int start = stream.getStart();
		final int line = stream.getLine();
		final int character = stream.getColumn();
		
		TokenType type = null;
		final char next = stream.next();
		switch(next) {
			case '.': type = TokenType.DOT; break;
			case '(': type = TokenType.PAREN_OPEN; break;
			case ')': type = TokenType.PAREN_CLOSE; break;
			case '[': type = TokenType.BRACKET_OPEN; break;
			case ']': type = TokenType.BRACKET_CLOSE; break;
			case '{': type = TokenType.BRACE_OPEN; break;
			case '}': type = TokenType.BRACE_CLOSE; break;
			case '*': type = TokenType.TIMES; break;
			case '/': type = TokenType.DIVIDES; break;
			case '%': type = TokenType.MOD; break;
			case ',': type = TokenType.COMMA; break;
			case '^': type = TokenType.BIT_XOR; break;
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
					if(isValidSecondLetterForIdentifier(char3)) {
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
					if(isValidSecondLetterForIdentifier(char4)) {
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
						type = TokenType.BIT_AND;
						stream.pushback(char1);
					} else {
						type = TokenType.AND;
					}
				}
				break;
				
			case '|':
				if(stream.hasNext()) {
					char char1 = stream.next();
					if(char1 != '|') {
						type = TokenType.BIT_OR;
						stream.pushback(char1);
					} else {
						type = TokenType.OR;
					}
				}
				break;
		}
		if(type != null) {
			return new StatementNodeImpl(start, line, character, type, null);
		}
		
		if(isValidFirstLetterForIdentifier(next)) {
			
			// ID
			PositionStringBuilder builder = new PositionStringBuilder(start, line, character);
			builder.append(next);
			while(stream.hasNext()) {
				char char1 = stream.next();
				if(isValidSecondLetterForIdentifier(char1)) {
					builder.append(char1);
				} else {
					stream.pushback(char1);
					break;
				}
			}
			return new StatementNodeImpl(start, line, character, TokenType.ID, builder.toPositionString());
			
		} else if(isDigit(next)) {
			
			// INT / REAL
			
			// 0 = no | 1 = '.' was last read character | 2 = yes
			int hasDot = 0;
			
			// 0 = no | 1 = '.' was last read character | 2 = '+'/'-' was last read character | 3 = yes
			int hasScale = 0;
			
			final StringBuilder string = new StringBuilder();
			string.append(next);
			while(stream.hasNext()) {
				char char1 = stream.next();
				if(isWhitespace(char1)) {
					break; // no need to push back a whitespace
				} else if(isDigit(char1)) {
					if(hasDot == 1) {
						hasDot = 2;
					} else if(hasScale == 1 || hasScale == 2) {
						hasScale = 3;
					}
				} else if(hasDot == 0 && char1 == '.') {
					hasDot = 1;
				} else if(hasScale == 0 && (char1 == 'e' || char1 == 'E')) {
					hasScale = 1;
				} else if(hasScale == 1 && (char1 == '+' || char1 == '-')) {
					hasScale = 2;
				} else {
					// I have read something that was not part of this very number.
					stream.pushback(char1);
					break;
				}
				string.append(char1);
			}
			
			// awaited more?
			if(hasDot == 1 || hasScale == 1 || hasScale == 2) {
				return new StatementNodeImpl(stream, TokenType.ERROR, null);
			}
			
			final BigDecimal result = new BigDecimal(string.toString());
			if(hasDot == 0 && hasScale == 0) {
				type = TokenType.INT;
			} else {
				type = TokenType.FLOAT;
			}
			return new StatementNodeImpl(start, line, character, type, result);
			
		} else if(next == '"') {

			boolean escaped = false;
			final StringBuilder string = new StringBuilder();
			string.append(next);
			for(;;) {
				if(!stream.hasNext()) {
					return new StatementNodeImpl(stream, TokenType.ERROR, null);
				}
				final char char1 = stream.next();
				if(escaped) {
					escaped = false;
				} else if(char1 == '\\') {
					escaped = true;
				} else if(char1 == '"') {
					string.append(char1);
					break;
				}
				string.append(char1);
			}
			return new StatementNodeImpl(start, line, character, TokenType.STRING, string.toString());
			
		} else if(next == '\'') {

			boolean escaped = false;
			final StringBuilder string = new StringBuilder();
			string.append(next);
			for(;;) {
				if(!stream.hasNext()) {
					return new StatementNodeImpl(stream, TokenType.ERROR, null);
				}
				final char char1 = stream.next();
				if(escaped) {
					escaped = false;
				} else if(char1 == '\\') {
					escaped = true;
				} else if(char1 == '\'') {
					string.append(char1);
					break;
				}
				string.append(char1);
			}
			return new StatementNodeImpl(start, line, character, TokenType.STRING, string.toString());
			
		} else {
		
			return new StatementNodeImpl(start, line, character, TokenType.ERROR, null);
		
		}
		
	}
	
	protected boolean hitEof = false;

	@Override
	public boolean hasNext() {
		if(nextNodeToRead != null) {
			return true;
		}
		try {
			nextNodeToRead = primitiveGetNext();
		} catch (IOException e) {
			throw new RuntimeException(e); // *should not* throw
		}
		if(nextNodeToRead == null && !hitEof) {
			hitEof = true;
			nextNodeToRead = new StatementNodeImpl(stream, TokenType.EOF, null);
		}
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
