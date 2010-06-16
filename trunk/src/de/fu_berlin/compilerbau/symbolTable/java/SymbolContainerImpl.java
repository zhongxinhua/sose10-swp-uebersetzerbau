package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.PositionString;


abstract class SymbolContainerImpl extends SymbolImpl implements SymbolContainer {
	
	SymbolContainerImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
	}
	
	@Override
	public Symbol getQualifiedSymbol(PositionString name, SymbolType type) {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, type);
		return lookup(uniqualifiedSymbol);
	}
	
	@Override
	public Symbol getQualifiedSymbol(PositionString name) {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name);
		return lookup(uniqualifiedSymbol);
	}
	
	@Override
	public boolean hasUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return getUnqualifiedSymbols().size() > 0;
	}
	
}
