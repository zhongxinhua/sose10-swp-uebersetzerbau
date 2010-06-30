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

import java.util.Iterator;
import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;


abstract class SymbolContainerImpl extends SymbolImpl implements SymbolContainer {
	
	SymbolContainerImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
		if(runtime != null) {
			runtime.registerSymbolContainer(this);
		}
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, this, type);
		QualifiedSymbol result = lookTreeUp(uniqualifiedSymbol);
		if(result.hasType(type)) {
			return result;
		} else {
			return null;
		}
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, this);
		return lookTreeUp(uniqualifiedSymbol);
	}
	
	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws InvalidIdentifierException{
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this, likeliness);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}
	
	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this, type);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}

	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}
	
}
