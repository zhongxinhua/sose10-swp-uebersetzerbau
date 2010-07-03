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

import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.util.PositionString;

public class InvalidIdentifierException extends SymbolTableException {
	
	private static final long serialVersionUID = -2790725272527032269L;
	
	protected final SymbolContainer container;
	protected final PositionString name;

	public InvalidIdentifierException(SymbolContainer container, PositionString name) {
		super(createMessageFor(container, name));
		this.container = container;
		this.name = name;
	}

	public static String createMessageFor(SymbolContainer container, PositionString name) {
		return "Invalid name " + name + " occured in " + container + ".";
	}
	
	public SymbolContainer getContainer() {
		return container;
	}

	public PositionString getName() {
		return name;
	}

}
