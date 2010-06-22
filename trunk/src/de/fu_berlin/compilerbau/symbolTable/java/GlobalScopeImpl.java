package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class GlobalScopeImpl implements SymbolContainer {
	
	protected final RuntimeImpl runtime;

	public GlobalScopeImpl(RuntimeImpl runtime) {
		this.runtime = runtime;
	}

	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		QualifiedSymbol result = lookTreeDown(symbol);
		if(result != null) {
			return result;
		}
		return runtime.lookTreeUp(symbol);
	}

	@Override
	public List<SymbolContainer> qualifyAllSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, Iterator<Entry<SymbolType, Likelyness>> likeliness) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMention(Symbol who, StreamPosition where) {
		// void
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Entry<Symbol, StreamPosition>> getMentions() {
		return Collections.EMPTY_SET;
	}

	@Override
	public SymbolContainer getParent() {
		return runtime;
	}

	@Override
	public Runtime getRuntime() {
		return runtime;
	}

	@Override
	public Boolean hasType(SymbolType leftType) {
		return null;
	}

	@Override
	public int compareTo(Symbol o) {
		throw new UnsupportedOperationException();
	}

}
