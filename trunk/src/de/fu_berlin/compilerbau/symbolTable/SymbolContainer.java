package de.fu_berlin.compilerbau.symbolTable;

import java.util.Collection;
import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {
	
	List<Symbol> getContainedSymbols();
	
	/**
	 * recursively
	 * @return immutable list
	 */
	List<UnqualifiedSymbol> getUnqualifiedSymbols();
	
	/**
	 * recursively
	 */
	boolean hasUnqualifiedSymbols();
	
	/**
	 * Returns a list of all symbols occurring twice in the same scope of visibility.
	 * @return immutable list
	 */
	Collection<Collection<Symbol>> getShadowedSymbols();
	
	/**
	 * Looks up this {@link UnqualifiedSymbol unqualified symbol}.
	 * @param symbol symbol to lookup
	 * @return null if not found
	 */
	Symbol lookup(UnqualifiedSymbol symbol);
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUniqualifiedSymbol(PositionString, SymbolType)
	 */
	Symbol getQualifiedSymbol(PositionString name, SymbolType type);
	
}
