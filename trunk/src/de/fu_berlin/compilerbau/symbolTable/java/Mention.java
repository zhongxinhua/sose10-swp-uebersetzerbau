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

import java.util.Map;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunResult;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunc;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.StreamPosition;

class Mention implements Map.Entry<Symbol, StreamPosition>, Comparable<Map.Entry<Symbol, StreamPosition>> {
	
	protected Symbol from;
	protected final StreamPosition position;
	
	public Mention(Symbol from, StreamPosition position) {
		this.position = position;
		this.from = from;

		if(from.hasType(SymbolType.CLASS) == null) {
			ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
				
				@Override
				public ReplaceFunResult replace()
						throws DuplicateIdentifierException,
						ShadowedIdentifierException,
						WrongModifierException,
						InvalidIdentifierException {
					final QualifiedSymbol qualifiedSymbol = ((UnqualifiedSymbol)Mention.this.from).qualify();
					if(qualifiedSymbol != null) {
						Mention.this.from = qualifiedSymbol;
						return ReplaceFunResult.REPLACED;
					} else {
						return ReplaceFunResult.NOT_REPLACED;
					}
				}
				
			};
			from.getRuntime().getUnqualifiedSymbolsMap().addUnqualifiedSymbol((UnqualifiedSymbol) from, replaceFunc);
		}
	}

	@Override
	public Symbol getKey() {
		return from;
	}

	@Override
	public StreamPosition getValue() {
		return position;
	}

	@Override
	public StreamPosition setValue(StreamPosition value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Entry<Symbol, StreamPosition> right) {
		final int l = position.getLine() - right.getValue().getLine();
		if(l != 0) {
			return l;
		}
		final int c = position.getColumn() - right.getValue().getColumn();
		if(c != 0) {
			return c;
		} else if(from == right.getKey()) {
			return 0;
		} else {
			return from.toString().compareTo(right.toString());
		}
	}

}
