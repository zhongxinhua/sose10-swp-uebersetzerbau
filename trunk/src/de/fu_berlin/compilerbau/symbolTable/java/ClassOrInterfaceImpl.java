package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ClassOrInterfaceImpl extends SymbolContainerImpl implements ClassOrInterface, Comparable<ClassOrInterfaceImpl> {
	
	protected final PositionString canonicalName;
	protected final Modifier modifier;

	public ClassOrInterfaceImpl(Runtime runtime, SymbolContainer parent, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) {
		// implements_
		super(runtime, parent);
		this.modifier = modifier;
		this.canonicalName = canonicalName;
	}

	@Override
	public Method addMethod(PositionString name, Symbol resultType,
			Iterator<Symbol> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCanonicalName() {
		return canonicalName.toString();
	}

	@Override
	public String getJavaSignature() {
		return "L" + canonicalName + ";";
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CLASS_OR_INTERFACE;
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

}
