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

import java.util.Iterator;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Base type of {@link Class classes}, {@link Interface interfaces},
 * {@link PrimitiveType primitive types} and {@link Void void types}.
 * @author rene
 */
public interface ClassOrInterface extends SymbolContainer, QualifiedSymbol, HasComparator<ClassOrInterface> {
	
	/**
	 * For interfaces you must not add a method body.
	 */
	Method addMethod(PositionString name, Symbol resultType, Iterator<Variable> parameters,
			Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * If this if an {@link Interface} itself, read getExtendedInterfaces ...
	 */
	Set<Symbol> getImplementedInterfaces();
	
	boolean isSame(ClassOrInterface right) throws InvalidIdentifierException;
	
	/**
	 * @return empty set or null if name was not found
	 */
	Set<Method> getMethodsByName(PositionString name);
	
}
