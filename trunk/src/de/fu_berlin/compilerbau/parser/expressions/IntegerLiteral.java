package de.fu_berlin.compilerbau.parser.expressions;

public class IntegerLiteral extends Literal {
	private int value;
	
	public IntegerLiteral(Number value) {
		this.value = value.intValue();
	}

	@Override
	public Type getType() {
		return Type.INTEGER;
	}
	
	@Override
	public String toString() {
		return "integer("+Integer.toString(value)+")";
	}
}
