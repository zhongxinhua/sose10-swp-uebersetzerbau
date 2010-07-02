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

package de.fu_berlin.compilerbau.statementLexer;

import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * A single lexem.
 * @author rene
 */
public interface StatementNode extends StreamPosition {
	
	TokenType getType();
	
	/**
	 * result will be an instance of for specific TokenType:
	 * <ul>
	 *   <li>ID, STRING: CharSequence</li>
	 *   <li>INT, REAL: Number</li>
	 *   <li>otherwise: null</li>
	 * </ul>
	 * @throws IllegalAccessException called for an invalid type
	 */
	Object getValue() throws IllegalAccessException;
	
	/**
	 * @return same as {@link #getType()}
	 * @throws IllegalAccessException called for an invalid type
	 */
	Number getNumber() throws IllegalAccessException;
	
	/**
	 * @return same as {@link #getType()}
	 * @throws IllegalAccessException called for an invalid type
	 */
	CharSequence getString() throws IllegalAccessException;
	
}
