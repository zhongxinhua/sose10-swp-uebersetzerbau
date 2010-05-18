package de.fu_berlin.compilerbau.parser;

import java.util.List;

public class DoStatement extends Statement {
	Expression test;
	List<Statement> body;
}