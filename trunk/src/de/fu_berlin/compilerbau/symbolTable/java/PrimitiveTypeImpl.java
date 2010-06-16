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
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.util.Visibility;

public class PrimitiveTypeImpl extends ClassOrInterfaceImpl implements PrimitiveType {

	protected final java.lang.Class<?> type;
	protected final String javaSignature;

	public PrimitiveTypeImpl(Runtime runtime, java.lang.Class<?> type) {
		super(runtime, runtime, null, GetModifier.getModifier(Visibility.PUBLIC, false, true, false),
				new PositionString(type.getName(), PositionBean.ZERO));
		this.type = type;
		
		if(type == boolean.class) javaSignature = "Z"; else
		if(type == char.class)    javaSignature = "C"; else
		if(type == double.class)  javaSignature = "D"; else
		if(type == float.class)   javaSignature = "F"; else
		if(type == int.class)     javaSignature = "I"; else
		if(type == long.class)    javaSignature = "J"; else
		if(type == short.class)   javaSignature = "S"; else
		if(type == boolean.class) javaSignature = "Z"; else
		if(type == boolean.class) javaSignature = "Z"; else
		throw new RuntimeException();
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
	public String getJavaSignature() {
		return javaSignature;
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

}
