package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * This {@link Symbol symbol} can contain other symbols.
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {

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
	 * Tries to convert all unqualified symbols to qualified ones.
	 * @return List of all containers having containing unqualified symbols* or
	 * 	null if all symbols are qualified. *) May be transitive or maybe not.
	 */
	List<SymbolContainer> qualifyAllSymbols();
	
	/**
	 * Tries to lookup a symbol. Returns unqualified symbol if not found.
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 * @see SymbolContainer#lookup(UnqualifiedSymbol)
	 */
	Symbol tryGetQualifiedSymbol(PositionString name, Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws InvalidIdentifierException;
	
	/**
	 * @see #tryGetQualifiedSymbol(PositionString, Iterator)
	 */
	Symbol tryGetQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException;

	/**
	 * @throws InvalidIdentifierException 
	 * @see #tryGetQualifiedSymbol(PositionString, Iterator)
	 */
	Symbol tryGetQualifiedSymbol(PositionString name) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUnqualifiedSymbol(PositionString, SymbolType)
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	@InternalMethod
	QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	@InternalMethod
	QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException;
	
	/**
	 * Being in a scope, looking downwards for a symbol.
	 */
	@InternalMethod
	QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException;

	/**
	 * Being in a scope, looking upwards for a symbol.
	 */
	@InternalMethod
	QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException;
	
}
