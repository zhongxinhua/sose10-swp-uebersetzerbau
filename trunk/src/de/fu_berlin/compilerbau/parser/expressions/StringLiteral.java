package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.util.PositionString;

public class StringLiteral extends Literal {
	CharSequence value;
	
	public StringLiteral(CharSequence value) {
		this.value = value;
	}

	@Override
	public Type getType() {
		return Type.STRING;
	}

	@Override
	public String toString() {
		return "string("+value+")";
	}
}
