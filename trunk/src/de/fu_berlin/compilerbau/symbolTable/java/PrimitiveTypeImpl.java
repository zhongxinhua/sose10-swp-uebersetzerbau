package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.util.Visibility;

class PrimitiveTypeImpl extends ClassOrInterfaceImpl implements PrimitiveType {

	protected static final Modifier DEFAULT_MODIFIER = GetModifier.getModifier(Visibility.PUBLIC, false, true, false);
	protected final java.lang.Class<?> type;
	protected final java.lang.Class<?> boxedType;

	public PrimitiveTypeImpl(Runtime runtime, java.lang.Class<?> type) throws InvalidIdentifierException {
		super(runtime, runtime.getGlobalScope(), null, DEFAULT_MODIFIER, new PositionString(type.getName(), PositionBean.ZERO));
		this.type = type;

		if (type == boolean.class) {
			boxedType = Boolean.class;
		} else if (type == byte.class) {
			boxedType = Byte.class;
		} else if (type == char.class) {
			boxedType = Character.class;
		} else if (type == double.class) {
			boxedType = Double.class;
		} else if (type == float.class) {
			boxedType = Float.class;
		} else if (type == int.class) {
			boxedType = Integer.class;
		} else if (type == long.class) {
			boxedType = Long.class;
		} else if (type == short.class) {
			boxedType = Short.class;
		} else if (type == Void.class) {
			boxedType = Void.class;
		} else {
			throw new RuntimeException();
		}
	}
	
	@Override
	public int compareKey() {
		return COMPARE_KEY;
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
	public Class<?> getJavaClass() {
		return type;
	}

	@Override
	public Class<?> getWrapperClass() {
		return boxedType;
	}

}
