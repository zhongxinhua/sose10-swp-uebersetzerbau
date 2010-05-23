package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.util.PositionString;

public class StringLiteral extends Literal {
	CharSequence value;
	
	public StringLiteral(CharSequence value) {
		this.value = value;
	}
}
