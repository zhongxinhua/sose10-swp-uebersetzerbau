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
