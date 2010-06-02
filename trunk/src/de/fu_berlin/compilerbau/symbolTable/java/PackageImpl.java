package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class PackageImpl extends SymbolContainerImpl implements Package, Comparable<PackageImpl> {
	
	final PositionString name;

	public PackageImpl(RuntimeImpl runtime, PositionString name) {
		super(runtime);
		this.name = name;
	}

	@Override
	public Class addClass(PositionString name, Symbol extends1,
			Iterator<Symbol> implements1, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interface addInterface(PositionString name,
			Iterator<Symbol> extends1, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Class> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ClassOrInterface> getClassesAndInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Interface> getInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(PackageImpl right) {
		return name.compareTo(right.name);
	}
	
	@Override
	public String toString() {
		return name.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
