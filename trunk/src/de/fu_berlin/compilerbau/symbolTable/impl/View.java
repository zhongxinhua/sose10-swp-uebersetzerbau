package de.fu_berlin.compilerbau.symbolTable.impl;

import java.util.Collection;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolTable;
import static de.fu_berlin.compilerbau.symbolTable.impl.CONSTANTS.*;

abstract class View extends SymbolTableImpl {
	
	final SymbolTableImpl _viewFor;
	
	View(SymbolTableImpl viewFor) {
		this._viewFor = viewFor;
	}
	
	@Override
	public Collection<Symbol> environmentOf() {
		return null;
	}
	
	@Override
	public SymbolTable getAncestors() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public SymbolTable getChildren() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public Collection<SymbolTable> getListOfChildren() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public SymbolTable getOffspring() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public SymbolTable getParent() throws NoSuchElementException {
		throw new NoSuchElementException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public SymbolTable hatch() throws UnsupportedOperationException {
		throw new UnsupportedOperationException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public SymbolTable hatch(Symbol child) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(WRONG_METHOD_FOR_VIEW);
	}
	
	@Override
	public Symbol isSymbol() {
		return null;
	}
	
	@Override
	public SymbolTable viewFor() {
		return _viewFor;
	}
	
	@Override
	public Symbol put(Symbol key, Symbol value) {
		throw new UnsupportedOperationException(WRONG_METHOD_FOR_VIEW);
	}
	
}
