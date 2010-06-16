package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class InterfaceImpl extends ClassOrInterfaceImpl implements Interface {
	
	protected final StreamPosition position;

	public InterfaceImpl(Runtime runtime, SymbolContainer parent, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) {
		super(runtime, parent, implements_, modifier, canonicalName);
		this.position = canonicalName;
	}

	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.INTERFACE;
	}

	@Override
	public Set<Set<? extends Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

}
