package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class ScopeImpl extends SymbolContainerImpl implements Scope {

	public ScopeImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Variable addVariable(PositionString name, Symbol type,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

}
