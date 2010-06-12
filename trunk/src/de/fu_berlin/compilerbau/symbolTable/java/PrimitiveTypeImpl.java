package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.Visibility;

public class PrimitiveTypeImpl extends ClassOrInterfaceImpl implements PrimitiveType {

	protected final java.lang.Class<?> type;

	public PrimitiveTypeImpl(Runtime runtime, java.lang.Class<?> type) {
		super(runtime, runtime, null, GetModifier.getModifier(Visibility.PUBLIC, false, true, false),
				new PositionString(type.getName(), PositionBean.ZERO));
		this.type = type;
	}

	@Override
	public Constructor addConstructor(Iterator<Symbol> parameters,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
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
		if(type == boolean.class) return "Z";
		if(type == char.class)    return "C";
		if(type == double.class)  return "D";
		if(type == float.class)   return "F";
		if(type == int.class)     return "I";
		if(type == long.class)    return "J";
		if(type == short.class)   return "S";
		if(type == boolean.class) return "Z";
		if(type == boolean.class) return "Z";
		throw new RuntimeException();
	}

}
