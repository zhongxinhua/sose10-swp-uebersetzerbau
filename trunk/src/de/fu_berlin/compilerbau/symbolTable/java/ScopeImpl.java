package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.CombinedSet;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

class ScopeImpl extends SymbolContainerImpl implements Scope {
	
	protected final Map<Variable,VariableImpl> variables = new TreeMap<Variable,VariableImpl>();
	protected final List<Scope> subScopes = new LinkedList<Scope>();
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);

	public ScopeImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
	}

	@Override
	public Variable addVariable(PositionString name, Symbol type,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		VariableImpl newSymbol = new VariableImpl(getRuntime(), this, name, modifier);
		final VariableImpl oldSymbol = variables.get(newSymbol);
		if(oldSymbol != null) {
			throw new DuplicateIdentifierException(this, newSymbol, oldSymbol);
		}
		shadowedSymbols.test(name, newSymbol);
		variables.put(newSymbol, newSymbol);
		return newSymbol;
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return shadowedSymbols.list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Symbol> getContainedSymbols() {
		Set[] sets = subScopes.toArray(new Set[subScopes.size()+1]);
		sets[subScopes.size()] = variables.keySet();
		return new CombinedSet(sets);
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QualifiedSymbol lookup(UnqualifiedSymbol symbol) {
		if(symbol.is(SymbolType.VARIABLE) != Likelyness.IMPOSSIBLE) {
			VariableImpl needle = new VariableImpl(null, null, symbol.getCall(), null);
			VariableImpl result = variables.get(needle);
			if(result != null) {
				return result;
			}
		}
		return getParent().lookup(symbol);
	}

	@Override
	public Scope addScope() {
		return new ScopeImpl(getRuntime(), this);
	}

}
