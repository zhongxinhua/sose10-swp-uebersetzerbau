package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collection;
import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.PositionString;


class SymbolContainerImpl extends SymbolImpl implements SymbolContainer {
	
	@Override
	public List<Symbol> getContainedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Symbol getQualifiedSymbol(PositionString name, SymbolType type) {
		UnqualifiedSymbol uniqualifiedSymbol = getRuntime().getUniqualifiedSymbol(name, type);
		return lookup(uniqualifiedSymbol);
	}
	
	@Override
	public Collection<Collection<Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
