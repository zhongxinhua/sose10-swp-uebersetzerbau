package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.util.PositionString;

public class RealLiteral extends Literal {
	private float value;
	
	public RealLiteral(Number value) {
		this.value = value.floatValue();
	}
}
