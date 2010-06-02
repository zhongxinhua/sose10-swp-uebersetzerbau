package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * The Runtime contains all packages that will be available to
 * the {@link Package packages}. It contains all packages.
 * @author kijewski
 */
public interface Runtime extends SymbolContainer {
	
	/**
	 * @return (immutable) list
	 */
	Set<Package> getPackages();
	
	/**
	 * If set, an {@link ShadowedIdentifierException exception} is thrown when a symbol gets shadowed.
	 */
	boolean getThrowsAtShadowing();
	
	/**
	 * @see #getThrowsAtShadowing()
	 */
	void setThrowsAtShadowing(boolean throwsAtShadowing);
	
	/**
	 * Adds a new package.
	 * @param name The canonical name of the package.
	 * @param isStaticImport "Adopt" every visible symbol to this scope.
	 * @return a {@link Package} to fill top-down.
	 */
	Package addPackage(PositionString name, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
	/**
	 * Returns a {@link UnqualifiedSymbol unqualified symbol, i.e. a symbol who's actual type
	 * may be unknown and only guess work. Unqualified symbols do not belong to a
	 * {@link SymbolContainer container}.
	 * @param likeliness A list of "likelinesses" regarding a {@link SymbolType symbol type}.
	 * @return A unqualified symbol. Give it to a {@link SymbolContainer container}!
	 * @throws RuntimeException A symbol occurred twice.
	 */
	UnqualifiedSymbol getUniqualifiedSymbol(PositionString name,
			Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws RuntimeException;
	
	/**
	 * The actual type is known.
	 * @see #getUniqualifiedSymbol(PositionString, Iterator)
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolType type);
	
	/**
	 * Tries to convert all unqualified symbols to qualified ones.
	 * @return List of all containers having containing unqualified symbols* or
	 * 	null if all symbols are qualified. *) May be transitive or maybe not.
	 */
	Set<SymbolContainer> qualifyAllSymbols();
	
}
