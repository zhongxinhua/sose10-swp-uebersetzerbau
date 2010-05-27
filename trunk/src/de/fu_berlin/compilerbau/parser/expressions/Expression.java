package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.statementParser.impl.StatementParser;
import de.fu_berlin.compilerbau.util.PositionString;

public abstract class Expression {

	public static Expression build(PositionString attributeValue) {
		return (new StatementParser()).parse(attributeValue, false);
	}

	public abstract Type getType();
	
	public abstract void printTree(int deep);
}
