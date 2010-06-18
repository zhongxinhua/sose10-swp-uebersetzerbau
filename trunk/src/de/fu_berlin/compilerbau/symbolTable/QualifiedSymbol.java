package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.StreamPosition;

public interface QualifiedSymbol extends Symbol {
	
	/**
	 * Returns the pure name as defined in the source.
	 */
	String getName();
	
	/**
	 * Returns the mangled name to conform the destination system.
	 */
	String getDestinationName();
	
	SymbolType getType();
	
	Modifier getModifier();
	
	/**
	 * Returns the position where the symbol was implemented.
	 * @return null if position is unknown
	 */
	StreamPosition getPosition();
	
}
