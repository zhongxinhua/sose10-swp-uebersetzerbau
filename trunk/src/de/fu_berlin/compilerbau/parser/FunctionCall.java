package de.fu_berlin.compilerbau.parser;

import java.util.List;

public class FunctionCall extends Expression { // a(b,c)
	Function function;
	List<Expression> actualParams;
}