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

import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Once you call {@link #setActualSymbol(QualifiedSymbol)}, it <em>becomes</em>
 * this {@link Symbol symbol}!
 * 
 * <p/>You most likely won't need to access this type directly.
 */
public interface UnqualifiedSymbol extends Symbol {
	
	/**
	 * Does the symbol belong to {@code what}?
	 */
	Likelyness is(SymbolType what);
	
	PositionString getCall();

	/**
	 * Tries to find the qualified symbol for this unqualified symbol.
	 * @return null if not found
	 * @throws InvalidIdentifierException 
	 */
	QualifiedSymbol qualify(SymbolType type) throws InvalidIdentifierException;

	/**
	 * @see #qualify(SymbolType)
	 */
	QualifiedSymbol qualify() throws InvalidIdentifierException;
	
	/**
	 * Internal method to determine the type of the actual symbol.
	 */
	@InternalMethod
	Map<SymbolType,Likelyness> getLikelynessPerType();
	
	@InternalMethod
	SymbolContainer getContainer();
	
}
