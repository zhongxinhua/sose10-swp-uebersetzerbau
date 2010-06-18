package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;

import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Once you call {@link #setActualSymbol(QualifiedSymbol)}, it <em>becomes</em>
 * this {@link Symbol symbol}!
 * 
 * <p/>You most likely won't need to access this type directly.
 */
public interface UnqualifiedSymbol extends Symbol {
	
	/**
	 * Does the symbol belong to {@code what}?
	 */
	Likelyness is(SymbolType what);
	
	PositionString getCall();
	
	/**
	 * Internal method to determine the type of the actual symbol.
	 */
	Map<SymbolType,Likelyness> getLikelynessPerType();
	
	/**
	 * Internal method to set the actual symbol for an UnqualifiedSymbol symbol.
	 * Do not call this method twice.
	 */
	void setActualSymbol(QualifiedSymbol actualSymbol);
	
	QualifiedSymbol getActualSymbol();
	
}
