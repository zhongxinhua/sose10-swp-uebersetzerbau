package de.fu_berlin.compilerbau.parser;

import java.util.List;

public class ScopeStatement extends Statement {
	SymbolTable symbolTable;
	List<Statement> body;
}