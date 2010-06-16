package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ClassOrInterfaceImpl extends SymbolContainerImpl implements ClassOrInterface, Comparable<ClassOrInterfaceImpl> {
	
	protected final PositionString canonicalName;
	protected final Modifier modifier;
	protected final Set<Symbol> interfaces = new TreeSet<Symbol>();
	protected final Map<Method, MethodImpl> methods = new TreeMap<Method, MethodImpl>();

	public ClassOrInterfaceImpl(Runtime runtime, SymbolContainer parent, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) {
		// implements_
		super(runtime, parent);
		this.modifier = modifier;
		this.canonicalName = canonicalName;
		while(implements_.hasNext()) {
			interfaces.add(implements_.next());
		}
	}

	@Override
	public Method addMethod(PositionString name, Symbol resultType,
			Iterator<Symbol> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		final MethodImpl newMethod = new MethodImpl(getRuntime(), this, name, resultType, parameters, modifier);
		final MethodImpl oldMethod = methods.get(newMethod);
		if(oldMethod != null) {
			throw new DuplicateIdentifierException(this, newMethod, oldMethod);
		}
		methods.put(newMethod, newMethod);
		return newMethod;
	}

	@Override
	public String getCanonicalName() {
		return canonicalName.toString();
	}

	@Override
	public String getJavaSignature() {
		return "L" + canonicalName + ";"; // TODO ← das ist wahrscheinlich Unsinn :)
	}

	@Override
	final public int compareTo(ClassOrInterfaceImpl right) {
		return canonicalName.compareTo(right.canonicalName);
	}

	@Override
	public Modifier getModifier() {
		return modifier;
	}

	@Override
	public StreamPosition getPosition() {
		return canonicalName;
	}

	@Override
	public Set<Symbol> getInterfaces() {
		return interfaces;
	}

	@Override
	public Set<Set<? extends Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CLASS_OR_INTERFACE;
	}

	@Override
	public Set<? extends Symbol> getContainedSymbols() {
		return methods.keySet();
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	final public Symbol lookup(UnqualifiedSymbol symbol) {
		return null;
	}

}
