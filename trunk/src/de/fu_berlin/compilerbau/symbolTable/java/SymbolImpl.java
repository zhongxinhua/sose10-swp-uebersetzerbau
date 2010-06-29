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
import java.util.Set;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
	private final Runtime runtime;
	private final SymbolContainer parent;
	protected final Set<Map.Entry<Symbol, StreamPosition>> mentions = new TreeSet<Map.Entry<Symbol, StreamPosition>>();
	
	SymbolImpl(Runtime runtime, SymbolContainer parent) {
		this.runtime = runtime;
		this.parent = parent ;
	}
	
	@Override
	public void addMention(Symbol who, StreamPosition where) {
		Mention mention = new Mention(who, where);
		mentions.add(mention);
	}
	
	@Override
	public Set<Map.Entry<Symbol, StreamPosition>> getMentions() {
		return mentions;
	}
	
	@Override
	public SymbolContainer getParent() {
		return parent;
	}

	@Override
	public Runtime getRuntime() {
		return runtime;
	}
	
	@Override
	public Boolean hasType(SymbolType rightType) {
		if(rightType == null || !(this instanceof QualifiedSymbol)) {
			return null;
		}
		final SymbolType leftType = ((QualifiedSymbol)this).getType();
		final Boolean result = SymbolType.implicates(leftType, rightType);
		return result;
	}

	@Override
	public int compareTo(Symbol right) {
		return UnqualifiedSymbolImpl.compare(this, right);
	}
	
}
