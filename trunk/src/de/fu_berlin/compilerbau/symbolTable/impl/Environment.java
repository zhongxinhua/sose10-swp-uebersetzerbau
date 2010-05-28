package de.fu_berlin.compilerbau.symbolTable.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolTable;
import de.fu_berlin.compilerbau.util.PositionString;
import static de.fu_berlin.compilerbau.symbolTable.impl.CONSTANTS.*;

class Environment extends SymbolTableImpl implements SymbolTable {
	
	public Environment(Iterable<PositionString> imports) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Symbol> environmentOf() {
		SymbolTable offspring = getOffspring();
		Collection<Symbol> result = offspring.values();
		return result;
	}
	
	@Override
	public SymbolTable getAncestors() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_ENV);
	}
	
	@Override
	public SymbolTable getChildren() throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<SymbolTable> getListOfChildren() throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SymbolTable getOffspring() throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SymbolTable getParent() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_ENV);
	}
	
	@Override
	public SymbolTable hatch() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SymbolTable hatch(Symbol child) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Symbol isSymbol() {
		return null;
	}
	
	@Override
	public SymbolTable viewFor() {
		return null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Set<Entry<Symbol, Symbol>> entrySet() {
		return new IdentitySet<Symbol>(elementsSet);
	}
	
	@Override
	public Symbol get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		return elementsSet.isEmpty();
	}
	
	@Override
	public Set<Symbol> keySet() {
		return Collections.unmodifiableSet(elementsSet);
	}
	
	@Override
	public int size() {
		return elementsSet.size();
	}
	
	@Override
	public Collection<Symbol> values() {
		return Collections.unmodifiableSet(elementsSet);
	}
	
	@Override
	public Symbol put(Symbol key, Symbol value) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
