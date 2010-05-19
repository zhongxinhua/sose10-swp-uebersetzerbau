package de.fu_berlin.compilerbau.statementLexer.impl;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;

public class StatementNodeImpl implements StatementNode {

	private static final long serialVersionUID = -3350158359867002247L;
	protected final int start, line, character;
	protected final TokenType type;
	protected final Object value;
	
	StatementNodeImpl(int start, int line, int character, TokenType type, Object value) {
		this.start = start;
		this.line = line;
		this.character = character;
		this.type = type;
		this.value = value;
	}

	@Override
	public int getCharacter() {
		return character;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public TokenType getType() {
		return type;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Number getNumber() throws IllegalAccessException {
		if(TokenType.ID.equals(type) || TokenType.STRING.equals(type)) {
			return (Number)value;
		}
		throw new IllegalAccessException();
	}

	@Override
	public CharSequence getString() throws IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

}
