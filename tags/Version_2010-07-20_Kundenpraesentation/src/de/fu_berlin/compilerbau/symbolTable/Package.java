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
 * This is one single package. Packages lay flat in the {@link Runtime} as they
 * do not employ a tree structure in the JRE.
 * @author kijewski
 */
public interface Package extends SymbolContainer, QualifiedSymbol, HasComparator<Package> {
	
	/**
	 * @return immutable list
	 */
	Set<ClassOrInterface> getClassesAndInterfaces();
	
	/**
	 * @return immutable list
	 */
	Set<Class> getClasses();
	
	/**
	 * @return immutable list
	 */
	Set<Interface> getInterfaces();

	Interface addInterface(PositionString name, Iterator<Symbol> extends_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;

	Class addClass(PositionString name, Symbol extends_, Iterator<Symbol> implements_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
}
