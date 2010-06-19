package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;


abstract class SymbolContainerImpl extends SymbolImpl implements SymbolContainer {
	
	SymbolContainerImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, type);
		return lookup(uniqualifiedSymbol);
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name);
		return lookup(uniqualifiedSymbol);
	}
	
	@Override
	public boolean hasUnqualifiedSymbols() {
		return getUnqualifiedSymbols().size() > 0;
	}

	@Override
	public List<SymbolContainer> qualifyAllSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
