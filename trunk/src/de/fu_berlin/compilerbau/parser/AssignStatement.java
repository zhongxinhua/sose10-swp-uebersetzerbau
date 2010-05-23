package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.LeftHandSide;

public class AssignStatement extends Statement {
	LeftHandSide lvalue;
	Expression rvalue;
}