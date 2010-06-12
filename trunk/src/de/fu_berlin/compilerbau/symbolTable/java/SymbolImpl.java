package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
	private final Runtime runtime;
	private final SymbolContainer parent;
	
	SymbolImpl(Runtime runtime, SymbolContainer parent) {
		this.runtime = runtime;
		this.parent = parent ;
	}
	
	@Override
	public void addMention(Symbol who, StreamPosition where) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Set<Map.Entry<Symbol, StreamPosition>> getMentions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SymbolContainer getParent() {
		return parent;
	}
	
	@Override
	public StreamPosition getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runtime getRuntime() {
		return runtime;
	}
	
}
