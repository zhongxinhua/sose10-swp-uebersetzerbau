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

package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Void;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;

class VoidTypeImpl extends PrimitiveTypeImpl implements Void {

	public VoidTypeImpl(Runtime runtime) throws InvalidIdentifierException {
		super(runtime, java.lang.Void.class);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VOID;
	}
	
	@Override
	public String toString() {
		return "void";
	}

}
