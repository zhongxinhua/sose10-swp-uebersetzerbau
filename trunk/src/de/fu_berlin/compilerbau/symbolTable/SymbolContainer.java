package de.fu_berlin.compilerbau.symbolTable;

import java.util.Set;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {
	
	Set<Symbol> getContainedSymbols();
	
	/**
	 * recursively
	 * @return immutable list
	 */
	Set<UnqualifiedSymbol> getUnqualifiedSymbols();
	
	/**
	 * recursively
	 */
	boolean hasUnqualifiedSymbols();
	
	/**
	 * Returns a list of all symbols occurring twice in the same scope of visibility.
	 * @return [ [ occurrence ] ]
	 */
	Set<Set<Symbol>> getShadowedSymbols();
	
	/**
	 * Looks up this {@link UnqualifiedSymbol unqualified symbol}.
	 * @param symbol symbol to lookup
	 * @return null if not found
	 */
	Symbol lookup(UnqualifiedSymbol symbol);
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUniqualifiedSymbol(PositionString, SymbolType)
	 * @return null if not found
	 */
	Symbol getQualifiedSymbol(PositionString name, SymbolType type);
	
}
