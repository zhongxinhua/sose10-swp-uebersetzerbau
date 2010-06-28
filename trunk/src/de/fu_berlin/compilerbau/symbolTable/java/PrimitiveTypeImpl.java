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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

}
