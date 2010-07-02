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

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Null;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.StreamPosition;

class NullImpl extends SymbolImpl implements Null {

	public NullImpl(Runtime runtime) {
		super(runtime, runtime.getUndefinedScope());
	}

	@Override
	public String getCanonicalDestinationName() {
		return "null";
	}

	@Override
	public String getDestinationName() {
		return "null";
	}

	@Override
	public Modifier getModifier() {
		return null;
	}

	@Override
	public String getName() {
		return "null";
	}

	@Override
	public StreamPosition getPosition() {
		return PositionBean.ZERO;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.NULL;
	}

}
