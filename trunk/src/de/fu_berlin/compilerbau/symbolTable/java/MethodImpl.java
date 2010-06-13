package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.List;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class MethodImpl extends SymbolImpl implements Method {

	public MethodImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Symbol> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Symbol> getContainedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol getQualifiedSymbol(PositionString name, SymbolType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<UnqualifiedSymbol> getUnqualifiedSymbols() {
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
	public Modifier getModifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamPosition getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
