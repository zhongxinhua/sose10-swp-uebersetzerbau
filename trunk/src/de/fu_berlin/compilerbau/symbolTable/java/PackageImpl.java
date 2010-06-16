package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class PackageImpl extends SymbolContainerImpl implements Package, Comparable<PackageImpl> {
	
	final PositionString name;
	
	protected Map<ClassOrInterfaceImpl,ClassImpl> classes =
			new TreeMap<ClassOrInterfaceImpl,ClassImpl>();
	protected Map<ClassOrInterfaceImpl,InterfaceImpl> interfaces =
			new TreeMap<ClassOrInterfaceImpl,InterfaceImpl>();
	protected Map<ClassOrInterfaceImpl,ClassOrInterfaceImpl> classesAndInterfaces =
			new TreeMap<ClassOrInterfaceImpl,ClassOrInterfaceImpl>();

	public PackageImpl(Runtime runtime, PositionString name) {
		super(runtime, runtime);
		this.name = name;
	}

	@Override
	public Class addClass(PositionString name, Symbol extends_,
			Iterator<Symbol> implements_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		final ClassImpl newSymbol = new ClassImpl(getRuntime(), this, extends_, implements_, modifier, name);
		final Symbol duplicate = classesAndInterfaces.get(newSymbol);
		if(duplicate != null) {
			throw new DuplicateIdentifierException(this, newSymbol, duplicate);
		}
		classes.put(newSymbol, newSymbol);
		classesAndInterfaces.put(newSymbol, newSymbol);
		return newSymbol;
	}

	@Override
	public Interface addInterface(PositionString name,
			Iterator<Symbol> extends_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		final InterfaceImpl newSymbol = new InterfaceImpl(getRuntime(), this, extends_, modifier, name);
		final Symbol duplicate = classesAndInterfaces.get(newSymbol);
		if(duplicate != null) {
			throw new DuplicateIdentifierException(this, newSymbol, duplicate);
		}
		interfaces.put(newSymbol, newSymbol);
		classesAndInterfaces.put(newSymbol, newSymbol);
		return newSymbol;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class> getClasses() {
		return (Set<Class>)(Set<?>)classes.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ClassOrInterface> getClassesAndInterfaces() {
		return (Set<ClassOrInterface>)(Set<?>)classesAndInterfaces.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Interface> getInterfaces() {
		return (Set<Interface>)(Set<?>)interfaces.keySet();
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
		return name.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String getCanonicalName() {
		return name.toString();
	}

	@Override
	public String getJavaSignature() {
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.PACKAGE;
	}

	@Override
	public Modifier getModifier() {
		return null;
	}

	@Override
	public StreamPosition getPosition() {
		return name;
	}

	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Set<? extends Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends Symbol> getContainedSymbols() {
		return classesAndInterfaces.keySet();
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

}
