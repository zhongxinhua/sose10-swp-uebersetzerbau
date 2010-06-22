package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.Pair;

class KnownTypeIterator implements Iterator<Map.Entry<SymbolType, Likelyness>> {
	
	protected int current = 0;
	protected SymbolType actualtype;
	
	public KnownTypeIterator(SymbolType actualtype) {
		this.actualtype = actualtype;
	}
	
	@Override
	public boolean hasNext() {
		return current < SymbolType.values().length;
	}
	
	@Override
	public Map.Entry<SymbolType, Likelyness> next() throws NoSuchElementException {
		final SymbolType[] values = SymbolType.values();
		if(current >= values.length) {
			throw new NoSuchElementException();
		}
		final SymbolType symbolType = values[current ++];
		
		final Likelyness likelyness = SymbolType.implicates(actualtype, symbolType) == Boolean.TRUE ? Likelyness.YES : Likelyness.IMPOSSIBLE;
		return new Pair<SymbolType, Likelyness>(symbolType, likelyness);
	}
	
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
}
