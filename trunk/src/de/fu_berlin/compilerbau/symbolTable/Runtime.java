package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
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
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * Returns a {@link UnqualifiedSymbol unqualified symbol, i.e. a symbol who's actual type
	 * may be unknown and only guess work. Unqualified symbols do not belong to a
	 * {@link SymbolContainer container}.
	 * @param likeliness A list of "likelinesses" regarding a {@link SymbolType symbol type}.
	 * @return A unqualified symbol. Give it to a {@link SymbolContainer container}!
	 * @throws RuntimeException A symbol occurred twice.
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name,
			Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws RuntimeException;
	
	/**
	 * The actual type is known.
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolType type);
	
	/**
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name);
	
	/**
	 * Tries to convert all unqualified symbols to qualified ones.
	 * @return List of all containers having containing unqualified symbols* or
	 * 	null if all symbols are qualified. *) May be transitive or maybe not.
	 */
	Set<SymbolContainer> qualifyAllSymbols();
	
	/**
	 * returns the type representing void
	 */
	Void getVoid();
	
	PrimitiveType getPrimitiveType(java.lang.Class<?> c);
	
	List<Entry<QualifiedSymbol,Symbol>> getAllShadowsList();
	
	ArrayType getArrayType(Symbol componentType, int dimension);
	
	ArrayType getArrayType(java.lang.Class<?> clazz);
	
	/**
	 * This method makes the input name a valid identifier for Java.
	 * E.g. "Übersetzerbau" could become a puny code like name "$Zbersetzerbau$clb".
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/Name_mangling">Wikipedia</a>.
	 * @return null if the name could not be mangled.
	 */
	String mangleName(String name);
	
	/**
	 * Tests the given string if it is a valid identifier in Java.
	 */
	boolean isValidIdentifier(String id);
	
	boolean isNameManglingEnabled();
	
	void setNameManglingEnabled(boolean enabled);
	
	/**
	 * Returns the scope for all symbols that do not belong to a scope (int, A[], ...).
	 */
	Package getGlobalScope();
	
}
