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

package de.fu_berlin.compilerbau.symbolTable.exceptions;

/**
 * An exception regarding the {@link de.fu_berlin.compilerbau.symbolTable symbol table} occurred.
 * @author rene
 */
public class SymbolTableException extends Exception {
	
	private static final long serialVersionUID = -3810445745344223529L;

	public SymbolTableException() {
		super();
	}
	
	public SymbolTableException(String message) {
		super(message);
	}
	
	public SymbolTableException(Throwable cause) {
		super(cause);
	}
	
	public SymbolTableException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
