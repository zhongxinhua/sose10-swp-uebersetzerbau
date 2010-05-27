package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.util.PositionString;

public class FloatLiteral extends Literal {
	private float value;
	
	public FloatLiteral(Number value) {
		this.value = value.floatValue();
	}

	@Override
	public Type getType() {
		return Type.FLOAT;
	}
	
	@Override
	public String toString() {
		return "float("+Float.toString(value)+")";
	}
}
