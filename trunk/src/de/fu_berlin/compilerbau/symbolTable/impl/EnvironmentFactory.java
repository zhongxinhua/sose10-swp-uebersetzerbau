package de.fu_berlin.compilerbau.symbolTable.impl;

import de.fu_berlin.compilerbau.symbolTable.SymbolTable;
import de.fu_berlin.compilerbau.util.PositionString;

public class EnvironmentFactory {
	
	private EnvironmentFactory() {
		// void
	}
	
	public static SymbolTable newEnvironment(Iterable<PositionString> imports) {
		return new Environment(imports);
	}
	
}
