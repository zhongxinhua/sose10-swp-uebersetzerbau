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

import java.util.LinkedList;
import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.PositionStringBuilder;

class SymbolSplitter {
	
	public static interface QualifiedSymbolCtor {
		
		QualifiedSymbol newInstance(PositionString str) throws InvalidIdentifierException;
		
	}

	public static QualifiedSymbol lookup(
			Runtime runtime,
			SymbolContainer self,
			UnqualifiedSymbol symbol,
			Map<? extends QualifiedSymbol, ? extends SymbolContainer> containers,
			QualifiedSymbolCtor ctor
	) throws InvalidIdentifierException {
		
		final Map<SymbolType, Likelyness> likelynessPerType = symbol.getLikelynessPerType();

		final LinkedList<PositionString> list = symbol.getCall().split('.', -1);
		for(int i = list.size()-1; i > 0; --i) {
			final PositionString p0 = list.get(0);
			final PositionStringBuilder builder = new PositionStringBuilder(p0);
			builder.append(p0.toString());
			for(int h = 1; h < i; ++h) {
				builder.append('.');
				builder.append(list.get(h));
			}
			
			final QualifiedSymbol qs = ctor.newInstance(builder.toPositionString());
			final SymbolContainer container = containers.get(qs);
			if(container != null) {
				final PositionString p1 = list.get(i);
				final PositionStringBuilder subCall = new PositionStringBuilder(p1);
				subCall.append(p1);
				for(int h = i+1; h < list.size()-1; ++h) {
					subCall.append('.');
					subCall.append(list.get(h));
				}
				
				final PositionString subCallStr = subCall.toPositionString();
				final UnqualifiedSymbolImpl subCallSym =
						new UnqualifiedSymbolImpl(subCallStr, runtime, self, likelynessPerType);
				return container.lookTreeDown(subCallSym);
			}
		}
		
		return null;
		
	}
	
}
