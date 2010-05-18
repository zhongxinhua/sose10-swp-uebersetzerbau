package de.fu_berlin.compilerbau.parser;

public class AssignStatement extends Statement {
	LeftHandSide lvalue;
	Expression rvalue;
}