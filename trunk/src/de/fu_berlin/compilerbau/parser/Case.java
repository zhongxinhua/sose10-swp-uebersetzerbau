package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.parser.expressions.Expression;

public class Case {
	Expression test;
	List<Statement> body;
}