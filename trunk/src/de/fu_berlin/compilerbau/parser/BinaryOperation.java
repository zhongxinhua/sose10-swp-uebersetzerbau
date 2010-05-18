package de.fu_berlin.compilerbau.parser;

public class BinaryOperation extends Expression { // a+b
	enum BinaryOperator {
		ADD, MINUS, MUL, DIV, MOD, GREATERTHAN, LESSTHAN, GREATEREQUAL, LESSEQUAL, NOTEQUAL, EQUAL, AND, OR, XOR
	}
	BinaryOperator operator;
	Expression left;
	Expression right;
}