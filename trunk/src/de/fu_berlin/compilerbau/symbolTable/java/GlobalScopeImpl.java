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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class GlobalScopeImpl extends PackageImpl {
	
	protected final Map<ClassOrInterface,ClassOrInterface> cois =
			new TreeMap<ClassOrInterface,ClassOrInterface>(ClassOrInterfaceImpl.COMPARATOR);
	protected final Map<PositionString,QualifiedSymbol> staticImports =
			new TreeMap<PositionString,QualifiedSymbol>();

	public GlobalScopeImpl(RuntimeImpl runtime) throws InvalidIdentifierException {
		super(runtime, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.EMPTY_MAP;
	}
	
	protected final SymbolSplitter.QualifiedSymbolCtor coiCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public QualifiedSymbol newInstance(PositionString str)
				throws InvalidIdentifierException {
			return new ClassOrInterfaceImpl(getRuntime(), GlobalScopeImpl.this, null, null, str);
		}
		
	};
	
	protected final SymbolSplitter.QualifiedSymbolCtor positionStringNoopCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public Object newInstance(PositionString str) {
			return str;
		}
		
	};

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol == null) {
			return null;
		}
		QualifiedSymbol result = SymbolSplitter.lookup(getRuntime(), this, symbol, cois, coiCtor);
		if(result != null) {
			return result;
		}
		result = cois.get(new ClassOrInterfaceImpl(getRuntime(), this, null, null, symbol.getCall()));
		if(result != null) {
			return result;
		}
		result = staticImports.get(symbol.getCall());
		
		return result;
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
		final UnqualifiedSymbol pkgUSym = getRuntime().getUnqualifiedSymbol(pkgString, getRuntime(), SymbolType.PACKAGE);
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
			final UnqualifiedSymbol coiUSym = getRuntime().getUnqualifiedSymbol(
					new PositionString(coiString, path), getRuntime(), SymbolType.CLASS_OR_INTERFACE);
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
	
	protected void useStaticImport(QualifiedSymbol value, PositionString alias) throws DuplicateIdentifierException {
		final PositionString key;
		if(alias == null) {
			key = new PositionString(value.getDestinationName(), value.getPosition());
		} else {
			key = alias;
		}
		final QualifiedSymbol duplicate = staticImports.get(key);
		if(duplicate != null) {
			throw new DuplicateIdentifierException(this, value, duplicate);
		}
		staticImports.put(key, value);
	}

}
