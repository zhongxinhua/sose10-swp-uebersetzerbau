/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.symbolTable;

import java.util.Comparator;
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
	 * @return null, if name was null
	 * @throws RuntimeException A symbol occurred twice.
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container, Iterator<Map.Entry<SymbolType,Likelyness>> likeliness);
	
	/**
	 * The actual type is known.
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container, SymbolType type);
	
	/**
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 */
	UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container);
	
	/**
	 * returns the type representing void
	 */
	Void getVoid();
	
	/**
	 * returns the type representing null
	 */
	Null getNull();
	
	List<Entry<QualifiedSymbol,Symbol>> getAllShadowsList();
	
	ArrayType getArrayType(Symbol componentType, int dimension);
	
	/**
	 * This method translates the input name into a valid Java identifier.
	 * E.g. "Übersetzerbau" could become a puny code like name "$Zbersetzerbau$clb",
	 * "эксперимент" could become "$Ze1aaigmhjjgr5i".
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Name_mangling">Wikipedia</a>.
	 * @return null if the name could not be mangled.
	 */
	String mangleName(String name);
	
	/**
	 * Tests the given string if it is a valid identifier in Java.
	 */
	boolean isValidIdentifier(String id);
	
	/**
	 * Creates a new {@link Variable} to be used e.g. in
	 * {@link Class#addConstructor(de.fu_berlin.compilerbau.util.StreamPosition, Iterator, Modifier) addConstructor}.
	 * @param name name of the parameter
	 * @param variableType type of the parameter
	 * @param modifier
	 * @throws InvalidIdentifierException
	 */
	Variable getNewVariableForParameter(PositionString name, Symbol variableType, Modifier modifier) throws InvalidIdentifierException;
	
	/**
	 * Fills the {@link #getGlobalScope() global scope}.
	 * Call this method <strong>exactly once</strong>, immediately before you add own symbols.
	 * @param imports [(path, alias)], where the alias name may be null, indicating no renaming
	 * @throws DuplicateIdentifierException 
	 */
	void useImports(Iterator<Map.Entry<PositionString,PositionString>> imports) throws InvalidIdentifierException, DuplicateIdentifierException;
	
	/**
	 * Tries to convert all unqualified symbols to qualified ones.
	 * @return List of all unqualified symbols. null or empty indicates no unqualified symbols left.
	 * @throws InvalidIdentifierException 
	 * @throws WrongModifierException 
	 * @throws ShadowedIdentifierException 
	 * @throws DuplicateIdentifierException 
	 */
	Set<UnqualifiedSymbol> qualifyAllSymbols() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * @return immutable list
	 */
	Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols();
	
	boolean hasUnqualifiedSymbols();
	
	/**
	 * Returns {@link Comparator} comparing <code>E</code>
	 * @param <E> Class to compare: {@link ClassOrInterface}, {@link Method}, {@link Package} or {@link Variable}.
	 * @param clazz E.class
	 * @return null if the class was not expected to have a comparator
	 */
	<E extends HasComparator<E>> Comparator<E> getComparator(java.lang.Class<E> clazz);
	
	@InternalMethod
	UnqualifiedSymbolsMap<UnqualifiedSymbol> getUnqualifiedSymbolsMap();
	
	/**
	 * Guess what? Don't use it ...
	 * @throws InvalidIdentifierException 
	 * @throws DuplicateIdentifierException 
	 * @see #useImports(Iterator)
	 */
	@InternalMethod
	void useImport(PositionString path, PositionString alias) throws InvalidIdentifierException, DuplicateIdentifierException;
	
	/**
	 * Returns the scope containing all the symbols in the global scope (java.lang.*, imports ...)
	 */
	@InternalMethod
	SymbolContainer getGlobalScope();
	
	/**
	 * Returns the scope for all symbols that do not belong to a scope (int, A[], ...).
	 */
	@InternalMethod
	Package getUndefinedScope();
	
	/**
	 * To populate the scope list.
	 */
	@InternalMethod
	void registerSymbolContainer(SymbolContainer container);

	/**
	 * Determine if newly added symbols get mangled.
	 * @see #mangleName(String)
	 */
	@InternalMethod
	boolean isNameManglingEnabled();

	/**
	 * @see #isNameManglingEnabled()
	 */
	@InternalMethod
	void setNameManglingEnabled(boolean enabled);

	/**
	 * Return the symbol representing a primitive type, e.g. byte.
	 */
	@InternalMethod
	PrimitiveType getPrimitiveType(java.lang.Class<?> c);

	/**
	 * Return the symbol representing this array class.
	 */
	@InternalMethod
	ArrayType getArrayType(java.lang.Class<?> clazz);
	
}
