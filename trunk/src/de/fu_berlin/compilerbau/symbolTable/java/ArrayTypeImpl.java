package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ArrayTypeImpl extends ClassImpl implements ArrayType {
	
	protected final Symbol componentType;
	protected final int dimension;

	public ArrayTypeImpl(Runtime runtime, Symbol componentType, int dimension) throws InvalidIdentifierException {
		super(runtime, runtime.getGlobalScope(), null, null, null, null);
		this.componentType = componentType;
		this.dimension = dimension;
	}

	public ArrayTypeImpl(Runtime rt, Class<?> clazz) throws InvalidIdentifierException {
		this(rt, arrayClassInfo(rt,clazz).getKey(), arrayClassInfo(rt,clazz).getValue());
	}

	private static Pair<Symbol, Integer> arrayClassInfo(Runtime runtime, Class<?> clazz) {
		int i = 0;
		Class<?> ct;
		while((ct = clazz.getComponentType()) != null) {
			clazz = ct;
			++i;
		}
		final Symbol symbol = RuntimeFactory.javaToCompilerType(runtime, clazz);
		return new Pair<Symbol, Integer>(symbol, Integer.valueOf(i));
	}

	@Override
	public Constructor addConstructor(StreamPosition pos,
			Iterator<Symbol> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Symbol getSuperClass() {
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.PRIMITIVE_TYPE;
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.emptyMap();
	}

	@Override
	public Symbol getComponentType() {
		return componentType;
	}

	@Override
	public int getDimension() {
		return dimension;
	}
	
	@Override
	public String getDestinationName() {
		return null;
	}

}
