package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;

public class FunctionCall extends Expression { // a(b,c)
	public FunctionCall(PositionString attributeValue) {
		super(attributeValue);
		// TODO Auto-generated constructor stub
	}
	Function function;
	List<Expression> actualParams;
}