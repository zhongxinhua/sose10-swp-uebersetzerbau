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

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ConstructorImpl extends MethodImpl implements Constructor {
	
	protected static final String INIT = "<init>";
	
	public ConstructorImpl(Runtime runtime, ClassOrInterface parent, StreamPosition pos,
			Iterator<Variable> parameters, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent, new PositionString(INIT, pos), runtime.getVoid(), parameters, modifier);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CONSTRUCTOR;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(parent.getDestinationName());
		builder.append('.');
		builder.append(destionationName);
		builder.append('(');
		if(!parameters.isEmpty()) {
			Iterator<Variable> i = parameters.iterator();
			builder.append(i.next());
			while(i.hasNext()) {
				builder.append(", ");
				builder.append(i.next());
			}
		}
		builder.append(')');
		return builder.toString();
	}

}
