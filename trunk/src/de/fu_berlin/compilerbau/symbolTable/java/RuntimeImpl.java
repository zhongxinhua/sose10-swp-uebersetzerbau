package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.Punycode;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;


class RuntimeImpl extends SymbolContainerImpl implements Runtime {
	
	protected final Map<PackageImpl, PackageImpl> packages = new TreeMap<PackageImpl, PackageImpl>();
	protected final Map<PrimitiveType,PrimitiveTypeImpl> primitiveTypes = new HashMap<PrimitiveType, PrimitiveTypeImpl>();
	protected final Map<Class<?>,PrimitiveTypeImpl> primitiveTypesByClass = new HashMap<Class<?>, PrimitiveTypeImpl>();
	protected final Map<String,PrimitiveTypeImpl> primitiveTypesByName = new HashMap<String, PrimitiveTypeImpl>();
	protected final VoidTypeImpl voidType;
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);
	protected final List<Entry<QualifiedSymbol, Symbol>> allShadowsList = new LinkedList<Entry<QualifiedSymbol, Symbol>>();
	protected final Package globalScope;
	protected final List<SymbolContainer> symbolContainers = new LinkedList<SymbolContainer>();
	
	protected boolean mangle = true;
	
	protected void addPrimitiveClass(Class<?> c) throws InvalidIdentifierException {
		PrimitiveTypeImpl typeImpl = new PrimitiveTypeImpl(this, c);
		primitiveTypesByClass.put(c, typeImpl);
		primitiveTypesByName.put(c.getName(), typeImpl);
		primitiveTypes.put(typeImpl, typeImpl);
	}
	
	public RuntimeImpl() {
		super(null, null);
		try {
			this.globalScope = new PackageImpl(this, new PositionString("\000c\0001<GLOBAL>\000c", PositionBean.ZERO));
			this.voidType = new VoidTypeImpl(this);
			
			addPrimitiveClass(boolean.class);
			addPrimitiveClass(byte.class);
			addPrimitiveClass(char.class);
			addPrimitiveClass(double.class);
			addPrimitiveClass(float.class);
			addPrimitiveClass(int.class);
			addPrimitiveClass(long.class);
			addPrimitiveClass(short.class);
		} catch (InvalidIdentifierException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Package addPackage(PositionString name, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException {
		PackageImpl newSymbol = new PackageImpl(this, name);
		PackageImpl oldSymbol = packages.get(newSymbol);
		if(oldSymbol != null) {
			throw new DuplicateIdentifierException(this, newSymbol, oldSymbol);
		} else {
		}
		shadowedSymbols.test(name, newSymbol);
		packages.put(newSymbol, newSymbol);
		return newSymbol;
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
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name,
			Iterator<Map.Entry<SymbolType, Likelyness>> likeliness) throws RuntimeException {
		return new UnqualifiedSymbolImpl(name, this, likeliness);
	}
	
	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolType type) {
		KnownTypeIterator iterator = new KnownTypeIterator(type);
		return getUnqualifiedSymbol(name, iterator);
	}
	
	@Override
	public Runtime getRuntime() {
		return this;
	}
	
	SymbolSplitter.QualifiedSymbolCtor pkgCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public QualifiedSymbol newInstance(PositionString str) throws InvalidIdentifierException {
			return new PackageImpl(getRuntime(), str);
		}
		
	};
	
	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol.is(PRIMITIVE_TYPE) != IMPOSSIBLE) {
			PrimitiveTypeImpl result = primitiveTypesByName.get(symbol.getCall().toString());
			if(result != null) {
				return result;
			}
		}
		
		if(symbol.is(PACKAGE) != IMPOSSIBLE) {
			final PositionString call = symbol.getCall();
			
			final PackageImpl pkgResult = packages.get(new PackageImpl(this, call));
			if(pkgResult != null) {
				return pkgResult;
			}
		}
		
		return SymbolSplitter.lookup(this, this, symbol, packages, pkgCtor);
	}

	@Override
	public de.fu_berlin.compilerbau.symbolTable.Void getVoid() {
		return voidType;
	}

	@Override
	public PrimitiveType getPrimitiveType(Class<?> c) {
		return primitiveTypesByClass.get(c);
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return shadowedSymbols.list;
	}

	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name) {
		return new UnqualifiedSymbolImpl(name, this);
	}

	@Override
	public List<Entry<QualifiedSymbol, Symbol>> getAllShadowsList() {
		return allShadowsList;
	}

	@Override
	public ArrayType getArrayType(Symbol componentType, int dimension) {
		try {
			return new ArrayTypeImpl(this, componentType, dimension);
		} catch (InvalidIdentifierException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayType getArrayType(Class<?> clazz) {
		try {
			return new ArrayTypeImpl(this, clazz);
		} catch (InvalidIdentifierException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isValidIdentifier(String id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String mangleName(String name) {
		if(mangle) {
			return Punycode.encode(name);
		} else {
			return name;
		}
	}

	@Override
	public boolean isNameManglingEnabled() {
		return mangle;
	}

	@Override
	public void setNameManglingEnabled(boolean enabled) {
		this.mangle = enabled;
	}

	@Override
	public Package getGlobalScope() {
		return globalScope;
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerSymbolContainer(SymbolContainer container) {
		symbolContainers.add(container);
	}

	@Override
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol)
			throws InvalidIdentifierException {
		return lookTreeDown(symbol);
	}
	
}
