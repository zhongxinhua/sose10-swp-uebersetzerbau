package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.StreamPosition;

public interface QualifiedSymbol extends Symbol {
	
	String getName();
	
	String getJavaSignature();
	
	SymbolType getType();
	
	Modifier getModifier();
	
	/**
	 * Returns the position where the symbol was implemented.
	 * @return null if position is unknown
	 */
	StreamPosition getPosition();
	
}
