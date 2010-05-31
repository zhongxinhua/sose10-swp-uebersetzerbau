package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
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
	public List<Pair<Symbol, StreamPosition>> getMentions() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
