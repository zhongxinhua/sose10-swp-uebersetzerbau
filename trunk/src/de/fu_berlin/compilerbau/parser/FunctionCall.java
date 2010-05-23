package de.fu_berlin.compilerbau.parser;

import java.util.List;

public class FunctionCall extends Expression { // a(b,c)
	public FunctionCall(String attributeValue) {
		super(attributeValue);
		// TODO Auto-generated constructor stub
	}
	Function function;
	List<Expression> actualParams;
}