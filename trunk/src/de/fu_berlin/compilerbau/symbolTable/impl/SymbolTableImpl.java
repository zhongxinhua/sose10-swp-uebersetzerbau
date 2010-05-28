package de.fu_berlin.compilerbau.symbolTable.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolTable;
import static de.fu_berlin.compilerbau.symbolTable.impl.CONSTANTS.*;

abstract class SymbolTableImpl implements SymbolTable {
	
	final List<SymbolTableImpl> children = new LinkedList<SymbolTableImpl>();
	final Set<SymbolTableImpl> childrenSet = new TreeSet<SymbolTableImpl>();
	
	final List<Symbol> elements = new LinkedList<Symbol>();
	final Set<Symbol> elementsSet = new TreeSet<Symbol>();

	@Override
	public void clear() {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public Symbol remove(Object key) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}
	
	@Override
	public void putAll(Map<? extends Symbol, ? extends Symbol> m) {
		for(Entry<? extends Symbol, ? extends Symbol> i : m.entrySet()) {
			put(i.getKey(), i.getValue());
		}
	}
	
	@Override
	public boolean containsValue(Object value) {
		if(value == null) {
			throw new NullPointerException();
		}
		for(Symbol v : values()) {
			if(value.equals(v)) {
				return true;
			}
		}
		return false;
	}
	
}
