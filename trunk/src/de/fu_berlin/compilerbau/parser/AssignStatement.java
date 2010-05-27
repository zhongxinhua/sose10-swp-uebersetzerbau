package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.parser.expressions.Expression;

public class AssignStatement extends Statement {
	Expression lvalue;
	Expression rvalue;
}