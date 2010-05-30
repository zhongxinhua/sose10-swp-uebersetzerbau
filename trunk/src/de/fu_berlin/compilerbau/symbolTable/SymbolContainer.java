package de.fu_berlin.compilerbau.symbolTable;

import java.util.Collection;
import java.util.List;

/**
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {
	
	List<Symbol> getContainedSymbols();
	
	List<UnqualifiedSymbol> getUnqualifiedSymbols();
	
	boolean hasUnqualifiedSymbols();
	
	/**
	 * Returns a list of all symbols occurring twice in the same scope of visibility.
	 * @return immutable list
	 */
	Collection<Collection<Symbol>> getShadowedSymbols();
	
}
