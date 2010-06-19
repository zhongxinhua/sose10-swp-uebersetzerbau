package de.fu_berlin.compilerbau.symbolTable;


public interface Variable extends Symbol, QualifiedSymbol, Comparable<Variable> {
	
	Symbol getVariableType();
	
}
