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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class PackageImpl extends SymbolContainerImpl implements Package {
	
	protected final PositionString name;
	protected final String destinationName;
	
	protected Map<ClassOrInterfaceImpl,ClassImpl> classes =
			new TreeMap<ClassOrInterfaceImpl,ClassImpl>(ClassOrInterfaceImpl.COMPARATOR);
	protected Map<ClassOrInterfaceImpl,InterfaceImpl> interfaces =
			new TreeMap<ClassOrInterfaceImpl,InterfaceImpl>(ClassImpl.COMPARATOR);
	protected Map<ClassOrInterfaceImpl,ClassOrInterfaceImpl> classesAndInterfaces =
			new TreeMap<ClassOrInterfaceImpl,ClassOrInterfaceImpl>(InterfaceImpl.COMPARATOR);
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);

	public PackageImpl(Runtime runtime, PositionString name) throws InvalidIdentifierException {
		super(runtime, runtime);
		this.name = name;
		
		if(name != null) {
			boolean first = true;
			final StringBuilder destionationName = new StringBuilder();
			for(final String pathComponent : name.toString().split("\\.")) {
				if(!first) {
					destionationName.append('.');
				} else {
					first = false;
				}
				final String mangledName = runtime.mangleName(pathComponent);
				// TODO: falsche Stelle für die Überprüfung
				/*
				if(mangledName == null || !runtime.isValidIdentifier(mangledName)) {
					throw new InvalidIdentifierException(runtime, name);
				}
				*/
				destionationName.append(mangledName);
			}
			this.destinationName = destionationName.toString();
		} else {
			this.destinationName = null;
		}
	}

	@Override
	public Class addClass(PositionString name, Symbol extends_,
			Iterator<Symbol> implements_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		if(!getRuntime().isValidIdentifier(name.toString())) {
			throw new InvalidIdentifierException(this, name);
		}
		final ClassImpl newSymbol = new ClassImpl(getRuntime(), this, extends_, implements_, modifier, name);
		final Symbol duplicate = classesAndInterfaces.get(newSymbol);
		if(duplicate != null) {
			throw new DuplicateIdentifierException(this, newSymbol, duplicate);
		}
		
		shadowedSymbols.test(name, newSymbol);
		
		classes.put(newSymbol, newSymbol);
		classesAndInterfaces.put(newSymbol, newSymbol);
		
		return newSymbol;
	}

	@Override
	public Interface addInterface(PositionString name,
			Iterator<Symbol> extends_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		if(!getRuntime().isValidIdentifier(name.toString())) {
			throw new InvalidIdentifierException(this, name);
		}
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
	public String getName() {
		return name.toString();
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
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol == null) {
			return null;
		}
		
		QualifiedSymbol oldSymbol = new ClassImpl(getRuntime(), this, null, null, null, symbol.getCall());
		ClassOrInterfaceImpl result = classes.get(oldSymbol);
		if(result != null) {
			return result;
		}
		
		oldSymbol = new InterfaceImpl(getRuntime(), this, null, null, symbol.getCall());
		result = interfaces.get(oldSymbol);
		if(result != null) {
			return result;
		}
		
		return getRuntime().lookTreeUp(symbol);
	}
	
	protected final SymbolSplitter.QualifiedSymbolCtor classCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public QualifiedSymbol newInstance(PositionString str)
				throws InvalidIdentifierException {
			return new ClassImpl(getRuntime(), PackageImpl.this, null, null, null, str);
		}
		
	};
	protected final SymbolSplitter.QualifiedSymbolCtor interfaceCtor = new SymbolSplitter.QualifiedSymbolCtor() {

		@Override
		public QualifiedSymbol newInstance(PositionString str)
				throws InvalidIdentifierException {
			return new InterfaceImpl(getRuntime(), PackageImpl.this, null, null, str);
		}
		
	};

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol == null) {
			return null;
		}
		
		ClassOrInterfaceImpl impl = new ClassOrInterfaceImpl(getRuntime(), this, null, null, symbol.getCall());
		QualifiedSymbol result = classesAndInterfaces.get(impl);
		if(result != null) {
			return result;
		}
		
		result = SymbolSplitter.lookup(getRuntime(), this, symbol, classes, classCtor);
		if(result != null) {
			return result;
		}
		
		return SymbolSplitter.lookup(getRuntime(), this, symbol, interfaces, interfaceCtor);
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}
	
	protected static final Comparator<? super Package> COMPARATOR = new Comparator<Package>() {

		@Override
		public int compare(Package left, Package right) {
			return left.getDestinationName().compareTo(right.getDestinationName());
		}
		
	};

	@Override
	public Comparator<? super Package> comparator() {
		return COMPARATOR;
	}

	@Override
	public String getCanonicalDestinationName() {
		return getDestinationName();
	}

}
