package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.util.PositionString;

public class BinaryOperation extends Expression { // a+b
	public BinaryOperation(PositionString attributeValue) {
		super(attributeValue);
		// TODO Auto-generated constructor stub
	}
	enum BinaryOperator {
		ADD, MINUS, MUL, DIV, MOD, GREATERTHAN, LESSTHAN, GREATEREQUAL, LESSEQUAL, NOTEQUAL, EQUAL, AND, OR, XOR
	}
	BinaryOperator operator;
	Expression left;
	Expression right;
}