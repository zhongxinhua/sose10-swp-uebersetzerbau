package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class MethodImpl extends ScopeImpl implements Method {
	
	protected final ClassOrInterface parent;
	protected final PositionString name;
	protected final String destionationName;
	protected final Symbol resultType;
	protected final List<Symbol> parameters = new LinkedList<Symbol>();
	protected final Modifier modifier;

	public MethodImpl(Runtime runtime, ClassOrInterface parent, PositionString name, Symbol resultType,
			Iterator<Symbol> parameters, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent);
		this.parent = parent;
		this.name = name;
		if(name != null && !name.equals(ConstructorImpl.INIT)) {
			this.destionationName = runtime.mangleName(name.toString());
			if(destionationName == null) {
				throw new InvalidIdentifierException(this, name);
			}
		} else {
			this.destionationName = name.toString();
		}
		this.resultType = resultType;
		this.modifier = modifier;
		while(parameters.hasNext()) {
			Symbol e = parameters.next();
			this.parameters.add(e);
		}
	}

	@Override
	public List<Symbol> getParameters() {
		return parameters;
	}

	@Override
	public Symbol getReturnType() {
		return resultType;
	}

	@Override
	public QualifiedSymbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return name.toString();
	}

	@Override
	public Modifier getModifier() {
		return modifier;
	}

	@Override
	public StreamPosition getPosition() {
		return name;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.METHOD;
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
	public String getDestinationName() {
		return destionationName;
	}

	@Override
	public Scope getScope() {
		return this;
	}

}
