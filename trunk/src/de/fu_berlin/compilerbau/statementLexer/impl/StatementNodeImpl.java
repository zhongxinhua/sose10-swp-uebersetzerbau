package de.fu_berlin.compilerbau.statementLexer.impl;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class StatementNodeImpl extends PositionBean implements StatementNode {

	private static final long serialVersionUID = -3350158359867002247L;
	protected final TokenType type;
	protected final Object value;
	
	StatementNodeImpl(int start, int line, int character, TokenType type, Object value) {
		super(start, line, character);
		this.type = type;
		this.value = value;
	}
	
	StatementNodeImpl(StreamPosition pos, TokenType type, Object value) {
		super(pos);
		this.type = type;
		this.value = value;
	}

	@Override
	public TokenType getType() {
		return type;
	}

	@Override
	public Object getValue() throws IllegalAccessException {
		if(TokenType.ID.equals(type) || TokenType.STRING.equals(type) ||
				TokenType.INT.equals(type) || TokenType.REAL.equals(type)) {
			return value;
		}
		throw new IllegalAccessException();
	}

	@Override
	public CharSequence getString() throws IllegalAccessException {
		if(TokenType.ID.equals(type) || TokenType.STRING.equals(type)) {
			return (CharSequence)value;
		}
		throw new IllegalAccessException();
	}

	@Override
	public Number getNumber() throws IllegalAccessException {
		if(TokenType.INT.equals(type) || TokenType.REAL.equals(type)) {
			return (Number)value;
		}
		throw new IllegalAccessException();
	}

}
