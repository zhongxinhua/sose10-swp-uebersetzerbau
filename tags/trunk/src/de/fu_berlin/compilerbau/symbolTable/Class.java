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

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * This type represents a class, if not {@link Void} or {@link PrimitiveType}.
 * @author rene
 */
public interface Class extends ClassOrInterface {
	
	Member addMember(PositionString name, Symbol type, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * @see ClassOrInterface#addMethod(PositionString, Symbol, List, Modifier)
	 */
	Constructor addConstructor(StreamPosition pos, Iterator<Variable> parameters, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	Symbol getSuperClass();
	
	/**
	 * Returns the scope representing the the static blocks in a class.
	 */
	Scope getStaticBlock();
	
}
