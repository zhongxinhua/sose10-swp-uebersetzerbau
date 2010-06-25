package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
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
	 * Tries to find the qualified symbol for this unqualified symbol.
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	QualifiedSymbol qualify(SymbolType type) throws InvalidIdentifierException;

	/**
	 * @see #qualify(SymbolType)
	 */
	QualifiedSymbol qualify() throws InvalidIdentifierException;
	
	/**
	 * Internal method to determine the type of the actual symbol.
	 */
	@InternalMethod
	Map<SymbolType,Likelyness> getLikelynessPerType();
	
	@InternalMethod
	SymbolContainer getContainer();
	
}
