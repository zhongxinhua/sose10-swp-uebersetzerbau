package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionStringBuilder;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;


public class RuntimeImpl extends SymbolContainerImpl implements Runtime {
	
	final Map<PackageImpl, PackageImpl> packages = new TreeMap<PackageImpl, PackageImpl>();
	final Map<Class<?>,PrimitiveTypeImpl> primitiveTypes = new HashMap<Class<?>, PrimitiveTypeImpl>();
	final Map<String,PrimitiveTypeImpl> primitiveTypesByName = new HashMap<String, PrimitiveTypeImpl>();
	final VoidTypeImpl voidType = new VoidTypeImpl(this);
	
	void addPrimitiveClass(Class<?> c) {
		PrimitiveTypeImpl typeImpl = new PrimitiveTypeImpl(this, c);
		primitiveTypes.put(c, typeImpl);
		primitiveTypesByName.put(c.getName(), typeImpl);
	}
	
	RuntimeImpl() {
		super(null, null);
		addPrimitiveClass(boolean.class);
		addPrimitiveClass(byte.class);
		addPrimitiveClass(char.class);
		addPrimitiveClass(double.class);
		addPrimitiveClass(float.class);
		addPrimitiveClass(int.class);
		addPrimitiveClass(long.class);
		addPrimitiveClass(short.class);
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
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolType type) {
		KnownTypeIterator iterator = new KnownTypeIterator(type);
		return getUniqualifiedSymbol(name, iterator);
	}
	
	@Override
	public Runtime getRuntime() {
		return this;
	}
	
	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		final Likelyness isPrimitive = symbol.is(PRIMITIVE_TYPE);
		if(isPrimitive != IMPOSSIBLE) {
			PrimitiveTypeImpl result = primitiveTypesByName.get(symbol.getCall().toString());
			if(result != null) {
				return result;
			}
		}
		
		final Likelyness isPackage = symbol.is(PACKAGE);
		if(isPackage != IMPOSSIBLE) {
			final PositionString call = symbol.getCall();
			
			final PackageImpl pkgResult = packages.get(new PackageImpl(this, call));
			if(pkgResult != null || isPackage == YES) {
				return pkgResult;
			}
		}
		
		final LinkedList<PositionString> list = symbol.getCall().split('.', -1);
		for(int i = list.size()-1; i > 0; ++i) {
			final PositionString p0 = list.get(0);
			final PositionStringBuilder builder = new PositionStringBuilder(p0);
			builder.append(p0.toString());
			for(int h = 1; h < i; ++h) {
				builder.append('.');
				builder.append(list.get(h));
			}
			
			final PackageImpl pkg = packages.get(new PackageImpl(this, builder.toPositionString()));
			if(pkg != null) {
				final PositionString p1 = list.get(i);
				final PositionStringBuilder subCall = new PositionStringBuilder(p1);
				subCall.append(p1);
				for(int h = i+1; h < list.size()-1; ++h) {
					subCall.append('.');
					subCall.append(list.get(h));
				}
				final UnqualifiedSymbolImpl subCallSym =
						new UnqualifiedSymbolImpl(subCall.toPositionString(), this, symbol.getLikelynessPerType());
				return pkg.lookup(subCallSym);
			}
		}
		
		return null;
	}

	@Override
	public Set<SymbolContainer> qualifyAllSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Symbol getVoid() {
		return voidType;
	}

	@Override
	public PrimitiveType getPrimitiveType(Class<?> c) {
		return primitiveTypes.get(c);
	}

	@Override
	public Set<Set<? extends Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Symbol> getContainedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
