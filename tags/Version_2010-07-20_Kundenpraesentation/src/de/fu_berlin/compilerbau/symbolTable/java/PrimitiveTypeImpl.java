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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.util.Visibility;

class PrimitiveTypeImpl extends ClassOrInterfaceImpl implements PrimitiveType {

	protected static final Modifier DEFAULT_MODIFIER = GetModifier.getModifier(Visibility.PUBLIC, false, true, false);
	protected final java.lang.Class<?> type;
	protected final java.lang.Class<?> boxedType;

	public PrimitiveTypeImpl(Runtime runtime, java.lang.Class<?> type) throws InvalidIdentifierException {
		super(runtime, runtime.getUndefinedScope(), null, DEFAULT_MODIFIER, new PositionString(type.getName(), PositionBean.ZERO));
		this.type = type;

		if (type == boolean.class) {
			boxedType = Boolean.class;
		} else if (type == byte.class) {
			boxedType = Byte.class;
		} else if (type == char.class) {
			boxedType = Character.class;
		} else if (type == double.class) {
			boxedType = Double.class;
		} else if (type == float.class) {
			boxedType = Float.class;
		} else if (type == int.class) {
			boxedType = Integer.class;
		} else if (type == long.class) {
			boxedType = Long.class;
		} else if (type == short.class) {
			boxedType = Short.class;
		} else if (type == Void.class) {
			boxedType = Void.class;
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public Constructor addConstructor(StreamPosition pos,
			Iterator<Variable> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Symbol getSuperClass() {
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.PRIMITIVE_TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public Class<?> getJavaClass() {
		return type;
	}

	@Override
	public Class<?> getWrapperClass() {
		return boxedType;
	}
	
	@Override
	public String toString() {
		return type.getName();
	}

	@Override
	public Scope getStaticBlock() {
		return null;
	}

	@Override
	public String getCanonicalDestinationName() {
		return getDestinationName();
	}

	public static final Map<Class<?>, Set<String>> primitiveTypeCompatibility =
			new HashMap<Class<?>, Set<String>>();
	static {
		final HashSet<String> boolean_ = new HashSet<String>();
		primitiveTypeCompatibility.put(boolean.class, boolean_);
		boolean_.add(boolean.class.getCanonicalName());
		boolean_.add(Boolean.class.getCanonicalName());
		boolean_.add(Object.class.getCanonicalName());
		
		final HashSet<String> char_ = new HashSet<String>();
		primitiveTypeCompatibility.put(char.class, char_);
		char_.add(char.class.getCanonicalName());
		char_.add(Character.class.getCanonicalName());
		char_.add(Object.class.getCanonicalName());
		
		final HashSet<String> double_ = new HashSet<String>();
		primitiveTypeCompatibility.put(double.class, double_);
		double_.add(double.class.getCanonicalName());
		double_.add(Double.class.getCanonicalName());
		double_.add(Number.class.getCanonicalName());
		double_.add(Object.class.getCanonicalName());
		
		final HashSet<String> float_ = new HashSet<String>();
		primitiveTypeCompatibility.put(float.class, float_);
		float_.add(float.class.getCanonicalName());
		float_.add(Float.class.getCanonicalName());
		for(String e : double_) {
			float_.add(e);
		}
		
		final HashSet<String> long_ = new HashSet<String>();
		primitiveTypeCompatibility.put(float.class, long_);
		long_.add(long.class.getCanonicalName());
		long_.add(Long.class.getCanonicalName());
		for(String e : float_) {
			long_.add(e);
		}
		
		final HashSet<String> int_ = new HashSet<String>();
		primitiveTypeCompatibility.put(float.class, int_);
		int_.add(int.class.getCanonicalName());
		int_.add(Integer.class.getCanonicalName());
		for(String e : long_) {
			int_.add(e);
		}
		
		final HashSet<String> short_ = new HashSet<String>();
		primitiveTypeCompatibility.put(short.class, short_);
		short_.add(long.class.getCanonicalName());
		short_.add(Long.class.getCanonicalName());
		for(String e : int_) {
			short_.add(e);
		}
		
		final HashSet<String> byte_ = new HashSet<String>();
		primitiveTypeCompatibility.put(byte.class, short_);
		byte_.add(byte.class.getCanonicalName());
		byte_.add(Byte.class.getCanonicalName());
		for(String e : int_) {
			short_.add(e);
		}
	}

	public static Boolean canBeCastInto(PrimitiveType specialType, ClassOrInterface generalType) {
		final String canonicalDestinationName = generalType.getCanonicalDestinationName();
		final Set<String> set = primitiveTypeCompatibility.get(specialType);
		final boolean result = set.contains(canonicalDestinationName);
		return result;
	}

}
