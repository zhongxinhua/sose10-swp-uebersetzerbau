package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;


public class RuntimeImpl extends SymbolContainerImpl implements Runtime {
	
	protected final Map<PackageImpl, PackageImpl> packages = new TreeMap<PackageImpl, PackageImpl>();
	
	RuntimeImpl() {
		super(null);
	}
	
	@Override
	public Package addPackage(PositionString name, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException {
		PackageImpl newPackage = new PackageImpl(this, name);
		PackageImpl oldPackage = packages.get(newPackage);
		if(oldPackage == null) {
			packages.put(newPackage, newPackage);
			return newPackage;
		} else {
			throw new DuplicateIdentifierException(this, newPackage, oldPackage);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<Package> getPackages() {
		return (Set)Collections.unmodifiableSet(packages.keySet());
	}
	
	protected boolean throwsAtShadowing = false;
	
	@Override
	public boolean getThrowsAtShadowing() {
		return throwsAtShadowing;
	}
	
	@Override
	public void setThrowsAtShadowing(boolean throwsAtShadowing) {
		this.throwsAtShadowing = throwsAtShadowing;
	}
	
	@Override
	public UnqualifiedSymbol getUniqualifiedSymbol(PositionString name,
			Iterator<Map.Entry<SymbolType, Likelyness>> likeliness) throws RuntimeException {
		return new UnqualifiedSymbolImpl(name, this, likeliness);
	}
	
	@Override
	public UnqualifiedSymbol getUniqualifiedSymbol(PositionString name, SymbolType type) {
		KnownTypeIterator iterator = new KnownTypeIterator(type);
		return getUniqualifiedSymbol(name, iterator);
	}
	
	@Override
	public Runtime getRuntime() {
		return this;
	}
	
	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		Likelyness isPackage = symbol.is(SymbolType.PACKAGE);
		if(isPackage == Likelyness.YES) {
			if(symbol instanceof UnqualifiedSymbolImpl) {
				PackageImpl newPackage = new PackageImpl(this, ((UnqualifiedSymbolImpl) symbol).name);
				return packages.get(newPackage);
			} else {
				throw new ClassCastException();
			}
		} else if(isPackage == Likelyness.MAYBE) {
			// TODO
			return null;
		} else if(isPackage == Likelyness.IMPOSSIBLE) {
			return super.lookup(symbol);
		}
		throw new NullPointerException();
	}
	
}
