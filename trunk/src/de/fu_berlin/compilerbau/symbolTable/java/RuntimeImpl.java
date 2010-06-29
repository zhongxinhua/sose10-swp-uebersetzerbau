/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.parser.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.HasComparator;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Null;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.SymbolTableException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.Punycode;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;


class RuntimeImpl extends SymbolContainerImpl implements Runtime {
	
	protected final Map<PackageImpl, PackageImpl> packages = new TreeMap<PackageImpl, PackageImpl>(PackageImpl.COMPARATOR);
	protected final Map<PrimitiveType,PrimitiveTypeImpl> primitiveTypes = new HashMap<PrimitiveType, PrimitiveTypeImpl>();
	protected final Map<Class<?>,PrimitiveTypeImpl> primitiveTypesByClass = new HashMap<Class<?>, PrimitiveTypeImpl>();
	protected final Map<String,PrimitiveTypeImpl> primitiveTypesByName = new HashMap<String, PrimitiveTypeImpl>();
	protected final VoidTypeImpl voidType;
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);
	protected final List<Entry<QualifiedSymbol, Symbol>> allShadowsList = new LinkedList<Entry<QualifiedSymbol, Symbol>>();
	protected final UndefinedScope undefinedScope;
	protected final List<SymbolContainer> symbolContainers = new LinkedList<SymbolContainer>();
	protected final GlobalScopeImpl globalScope;
	protected final UnqualifiedSymbolsMap<UnqualifiedSymbol> unqualifiedSymbolsMap = new UnqualifiedSymbolsMapImpl<UnqualifiedSymbol>();
	protected final Null nullSymbol;
	
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
			this.undefinedScope = new UndefinedScope(this);
			this.globalScope = new GlobalScopeImpl(this);
			
			this.voidType = new VoidTypeImpl(this);
			this.nullSymbol = new NullImpl(this);
			
			globalScope.useStaticImport(nullSymbol, null);
			
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
		} catch (DuplicateIdentifierException e) {
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
	
	protected final UnqualifiedSymbolGenerator unqualifiedSymbolGenerator =
			new UnqualifiedSymbolGenerator(this);

	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container) {
		return unqualifiedSymbolGenerator.getUnqualifiedSymbol(name, container);
	}
	
	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container,
			Iterator<Map.Entry<SymbolType, Likelyness>> likeliness) throws RuntimeException {
		return unqualifiedSymbolGenerator.getUnqualifiedSymbol(name, container, likeliness);
	}
	
	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name, SymbolContainer container, SymbolType type) {
		return unqualifiedSymbolGenerator.getUnqualifiedSymbol(name, container, type);
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
		if(symbol == null) {
			return null;
		}
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
		
		final QualifiedSymbol result = SymbolSplitter.lookup(this, this, symbol, packages, pkgCtor);
		if(result != null) {
			return result;
		}
		return globalScope.lookTreeDown(symbol);
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
		if(isReservedIdentifier(id)) {
			return false;
		}
		
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
	
	public boolean isReservedIdentifier(String name){
		if(name.equals("break")){
			return true;
		}else if(name.equals("class")){
			return true;
		}else if(name.equals("continue")){
			return true;
		}else if(name.equals("do")){
			return true;
		}else if(name.equals("extends")){
			return true;
		}else if(name.equals("final")){
			return true;
		}else if(name.equals("float")){
			return true;
		}else if(name.equals("function")){
			return true;
		}else if(name.equals("implements")){
			return true;
		}else if(name.equals("import")){
			return true;
		}else if(name.equals("int")){
			return true;
		}else if(name.equals("interface")){
			return true;
		}else if(name.equals("module")){
			return true;
		}else if(name.equals("new")){
			return true;
		}else if(name.equals("return")){
			return true;
		}else if(name.equals("super")){
			return true;
		}else if(name.equals("this")){
			return true;
		}else if(name.equals("void")){
			return true;
		}else if(name.equals("while")){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void setNameManglingEnabled(boolean enabled) {
		this.mangle = enabled;
	}

	@Override
	public Package getUndefinedScope() {
		return undefinedScope;
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		return unqualifiedSymbolsMap.getUnqualifiedSymbols();
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

	@Override
	public Variable getNewVariableForParameter(PositionString name, Symbol variableType, Modifier modifier) throws InvalidIdentifierException {
		return new VariableImpl(this, null, name, variableType, modifier);
	}

	@Override
	public SymbolContainer getGlobalScope() {
		return globalScope;
	}

	@Override
	public void useImports(
			Iterator<Entry<PositionString, PositionString>> imports)
			throws InvalidIdentifierException, DuplicateIdentifierException {
		while(imports.hasNext()) {
			final Entry<PositionString, PositionString> next = imports.next();
			final PositionString path = next.getKey();
			final PositionString alias = next.getValue();
			useImport(path, alias);
		}
		try {
			Set<UnqualifiedSymbol> unqualifiedSymbols = qualifyAllSymbols();
			if(unqualifiedSymbols != null && !unqualifiedSymbols.isEmpty()) {
				throw new RuntimeException("The imports have (an) unqualified symbol(s): " + unqualifiedSymbols);
			}
		} catch (SymbolTableException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void useImport(PositionString path, PositionString alias) throws InvalidIdentifierException, DuplicateIdentifierException {
		globalScope.useImport(path, alias);
	}

	@Override
	public Set<UnqualifiedSymbol> qualifyAllSymbols() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException {
		return unqualifiedSymbolsMap.qualifyAllSymbols();
	}

	@Override
	public boolean hasUnqualifiedSymbols() {
		return unqualifiedSymbolsMap.hasUnqualifiedSymbols();
	}

	@Override
	public UnqualifiedSymbolsMap<UnqualifiedSymbol> getUnqualifiedSymbolsMap() {
		return unqualifiedSymbolsMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E extends HasComparator<E>> Comparator<E> getComparator(
			Class<E> clazz) {
		if(clazz == ClassOrInterface.class || clazz == ClassOrInterfaceImpl.class) {
			return (Comparator<E>) ClassOrInterfaceImpl.COMPARATOR;
		} else if(clazz == Method.class || clazz == MethodImpl.class) {
			return (Comparator<E>) MethodImpl.COMPARATOR;
		} else if(clazz == Package.class || clazz == PackageImpl.class) {
			return (Comparator<E>) PackageImpl.COMPARATOR;
		} else if(clazz == Variable.class || clazz == VariableImpl.class) {
			return (Comparator<E>) VariableImpl.COMPARATOR;
		} else {
			return null;
		}
	}

	@Override
	public Null getNull() {
		return nullSymbol;
	}
	
}
