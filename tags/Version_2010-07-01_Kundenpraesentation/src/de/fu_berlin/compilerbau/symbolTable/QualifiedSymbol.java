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

import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * This symbol is not only referenced but already declared.
 * @author rene
 */
public interface QualifiedSymbol extends Symbol {
	
	/**
	 * Returns the pure name as defined in the source.
	 */
	String getName();
	
	/**
	 * Returns the mangled name to conform the destination system.
	 */
	String getDestinationName();
	
	/**
	 * Returns the destination name including the package name.
	 * @see #getDestinationName()
	 */
	String getCanonicalDestinationName();
	
	SymbolType getType();
	
	Modifier getModifier();
	
	/**
	 * Returns the position where the symbol was implemented.
	 * @return null if position is unknown
	 */
	StreamPosition getPosition();
	
}
