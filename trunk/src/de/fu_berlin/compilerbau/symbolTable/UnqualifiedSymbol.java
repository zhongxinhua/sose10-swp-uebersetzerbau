package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.Likelyness;


public interface UnqualifiedSymbol extends Symbol {
	
	/**
	 * Does the symbol belong to {@code what}?
	 */
	Likelyness is(SymbolType what);
	
}
