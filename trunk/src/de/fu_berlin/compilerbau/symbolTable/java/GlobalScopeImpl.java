package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class GlobalScopeImpl extends PackageImpl {
	
	protected final Map<ClassOrInterface,ClassOrInterface> cois =
			new TreeMap<ClassOrInterface,ClassOrInterface>(ClassOrInterfaceImpl.COMPARATOR);

	public GlobalScopeImpl(RuntimeImpl runtime) throws InvalidIdentifierException {
		super(runtime, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected final SymbolSplitter.QualifiedSymbolCtor coiCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public QualifiedSymbol newInstance(PositionString str)
				throws InvalidIdentifierException {
			return new ClassOrInterfaceImpl(getRuntime(), GlobalScopeImpl.this, null, null, str);
		}
		
	};

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		SymbolSplitter.lookup(getRuntime(), this, symbol, cois, coiCtor);
		return null;
	}

	@Override
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		QualifiedSymbol result = lookTreeDown(symbol);
		if(result != null) {
			return result;
		}
		return getRuntime().lookTreeUp(symbol);
	}

	@Override
	public List<SymbolContainer> qualifyAllSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean hasType(SymbolType leftType) {
		return null;
	}

	@Override
	public int compareTo(Symbol right) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "<global scope>";
	}

	public void useImport(PositionString path, PositionString alias) throws InvalidIdentifierException, DuplicateIdentifierException {
		int lastColon = path.toString().lastIndexOf('.');
		if(lastColon < 0) {
			throw new InvalidIdentifierException(this, path);
		}
		final String pkgName = path.toString().substring(0, lastColon);
		final PositionString pkgString = new PositionString(pkgName, path);
		final UnqualifiedSymbol pkgUSym = getRuntime().getUnqualifiedSymbol(pkgString, SymbolType.PACKAGE);
		final Package pkg = (Package) getRuntime().lookTreeDown(pkgUSym);
		if(pkg == null) {
			throw new InvalidIdentifierException(this, pkgString);
		}
		final String coiString = path.toString().substring(lastColon+1);
		if("*".equals(coiString)) {
			for(ClassOrInterface coi : pkg.getClassesAndInterfaces()) {
				useImport(coi, null);
			}
		} else {
			final UnqualifiedSymbol coiUSym = getRuntime().getUnqualifiedSymbol(new PositionString(coiString, path), SymbolType.CLASS_OR_INTERFACE);
			final ClassOrInterface coi = (ClassOrInterface) pkg.lookTreeDown(coiUSym);
			if(coi == null) {
				throw new InvalidIdentifierException(this, pkgString);
			}
			useImport(coi, alias);
		}
	}

	protected void useImport(ClassOrInterface value, PositionString alias) throws DuplicateIdentifierException, InvalidIdentifierException {
		
		final ClassOrInterface key;
		if(alias == null) {
			key = value;
		} else {
			key = new ClassOrInterfaceImpl(getRuntime(), this, null, null, alias);
		}
		final ClassOrInterface duplicate = cois.get(value);
		if(duplicate != null) {
			throw new DuplicateIdentifierException(this, value, duplicate);
		}
		cois.put(key, value);
	}

}
