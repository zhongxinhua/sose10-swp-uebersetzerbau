package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
	private final RuntimeImpl runtime;
	
	SymbolImpl(RuntimeImpl runtime) {
		this.runtime = runtime;
	}
	
	@Override
	public void addMention(Symbol who, StreamPosition where) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCanonicalName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getJavaSignature() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<Map.Entry<Symbol, StreamPosition>> getMentions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SymbolContainer getParent() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public SymbolType getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
