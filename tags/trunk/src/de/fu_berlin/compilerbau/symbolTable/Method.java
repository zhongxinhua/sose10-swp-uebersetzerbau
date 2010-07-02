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

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;

/**
 * This is a method of a {@link ClassOrInterface class or interface}.
 * {@link Constructor Constructors} are methods as well.
 * @author rene
 */
public interface Method extends Scope, QualifiedSymbol, HasComparator<Method> {
	
	List<Variable> getParameters();
	
	/**
	 * The return type of this method.
	 * Arbitrary for {@link Constructor constructors}!
	 * @return may be null for {@link Void} and  {@link Constructor constructors}
	 */
	Symbol getReturnType();
	
	/**
	 * The outmost scope of this method.
	 * @return most likely {@code this}
	 */
	Scope getScope();
	

	
	/**
	 * @return null if indeterminable
	 * @throws InvalidIdentifierException 
	 */
	Boolean isCompatatibleToParameters(Iterable<Symbol> parameterTypes) throws InvalidIdentifierException;
	
}
