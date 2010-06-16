package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {
	
	Set<? extends Symbol> getContainedSymbols();
	
	/**
	 * recursively
	 * @return immutable list
	 */
	Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols();
	
	/**
	 * recursively
	 */
	boolean hasUnqualifiedSymbols();
	
	/**
	 * Returns a list of all symbols occurring twice in the same scope of visibility.
	 * @return [ [ occurrence ] ]
	 */
	Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols();
	
	/**
	 * Looks up this {@link UnqualifiedSymbol unqualified symbol}.
	 * @param symbol symbol to lookup
	 * @return null if not found
	 */
	QualifiedSymbol lookup(UnqualifiedSymbol symbol);
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUnqualifiedSymbol(PositionString, SymbolType)
	 * @return null if not found
	 */
	Symbol getQualifiedSymbol(PositionString name, SymbolType type);
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @return null if not found
	 */
	Symbol getQualifiedSymbol(PositionString name);
	
}
