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
import java.util.Map;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

final class Binary implements Comparable<Binary> {

	final PositionString name;
	final SymbolContainer container;
	
	Binary(PositionString name, SymbolContainer container) {
		this.name = name;
		this.container = container;
	}
	
	@Override
	public int compareTo(Binary right) {
		if(container != right.container) {
			return System.identityHashCode(container) - System.identityHashCode(right.container);
		} else {
			return name.compareTo(right.name);
		}
	}
	
	static final Map<Binary, UnqualifiedSymbol> map = new TreeMap<Binary, UnqualifiedSymbol>();
	
}

final class Ternary implements Comparable<Ternary> {

	final PositionString name;
	final SymbolContainer container;
	final SymbolType type;
	
	Ternary(PositionString name, SymbolContainer container, SymbolType type) {
		this.name = name;
		this.container = container;
		this.type = type;
	}
	
	@Override
	public int compareTo(Ternary right) {
		if(type != right.type) {
			return type.compareTo(right.type);
		} else if(container != right.container) {
			return System.identityHashCode(container) - System.identityHashCode(right.container);
		} else {
			return name.compareTo(right.name);
		}
	}
	
	static final Map<Ternary, UnqualifiedSymbol> map = new TreeMap<Ternary, UnqualifiedSymbol>();
	
}

class UnqualifiedSymbolGenerator {
	
	protected final RuntimeImpl runtime;
	
	UnqualifiedSymbolGenerator(RuntimeImpl runtime) {
		this.runtime = runtime;
	}
	
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container) {
		if(name == null) {
			return null;
		}
		final Binary c = new Binary(name, container);
		UnqualifiedSymbol result = Binary.map.get(c);
		if(result == null) {
			result = new UnqualifiedSymbolImpl(name, runtime, container);
			Binary.map.put(c, result);
		}
		return result;
	}
	
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container,
			Iterator<Map.Entry<SymbolType, Likelyness>> likeliness) throws RuntimeException {
		return new UnqualifiedSymbolImpl(name, runtime, container, likeliness);
	}
	
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container, SymbolType type) {
		if(name == null) {
			return null;
		}
		final Ternary c = new Ternary(name, container, type);
		UnqualifiedSymbol result = Ternary.map.get(c);
		if(result == null) {
			final KnownTypeIterator iterator = new KnownTypeIterator(type);
			result = getUnqualifiedSymbol(name, container, iterator);
			Ternary.map.put(c, result);
		}
		return result;
	}
}
