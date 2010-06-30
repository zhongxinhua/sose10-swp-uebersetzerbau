package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SetOfMethods;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class SetOfMethodsImpl extends SymbolImpl implements SetOfMethods {
	
	protected final PositionString name;
	protected final Set<Method> methods;

	public SetOfMethodsImpl(Runtime runtime, SymbolContainer parent, PositionString name, Set<Method> methods) {
		super(runtime, parent);
		this.name = name;
		this.methods = methods;
	}

	@Override
	public String getCanonicalDestinationName() {
		return null;
	}

	@Override
	public String getDestinationName() {
		return null;
	}

	@Override
	public Modifier getModifier() {
		return null;
	}

	@Override
	public String getName() {
		return name.toString();
	}

	@Override
	public StreamPosition getPosition() {
		return name;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.SET_OF_METHODS;
	}

	@Override
	public Set<Method> getContainedMethods() {
		return methods;
	}

	@Override
	public List<Method> getContainedMethods(Iterable<Symbol> parameterTypes) throws InvalidIdentifierException {
		final ArrayList<Method> result = new ArrayList<Method>(methods.size());
		for(Method method : methods) {
			if(method.isCompatatibleToParameters(parameterTypes)) {
				result.add(method);
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return methods.toString();
	}

}
