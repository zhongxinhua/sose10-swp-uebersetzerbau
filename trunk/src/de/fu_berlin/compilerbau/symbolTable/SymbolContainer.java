package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * This {@link Symbol symbol} can contain other symbols.
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
	QualifiedSymbol lookup(UnqualifiedSymbol symbol) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUnqualifiedSymbol(PositionString, SymbolType)
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException;
	
}
