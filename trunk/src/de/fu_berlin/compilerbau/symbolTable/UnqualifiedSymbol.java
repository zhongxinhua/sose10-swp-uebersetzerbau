package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;

import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * You most likely won't need to access this type directly.
 */
public interface UnqualifiedSymbol extends Symbol {
	
	/**
	 * Does the symbol belong to {@code what}?
	 */
	Likelyness is(SymbolType what);
	
	PositionString getCall();
	
	Map<SymbolType,Likelyness> getLikelynessPerType();
	
}
