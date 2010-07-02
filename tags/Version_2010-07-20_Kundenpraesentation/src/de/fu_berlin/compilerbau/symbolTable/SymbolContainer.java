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

package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * This {@link Symbol symbol} can contain other symbols.
 * @author kijewski
 */
public interface SymbolContainer extends Symbol {
	
	/**
	 * Returns a list of all symbols occurring twice in the same scope of visibility.
	 * @return [ [ occurrence ] ]
	 */
	Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols();
	
	/**
	 * Tries to lookup a symbol. Returns unqualified symbol if not found.
	 * @see #getUnqualifiedSymbol(PositionString, Iterator)
	 * @see SymbolContainer#lookup(UnqualifiedSymbol)
	 * @return null if name was null
	 */
	Symbol tryGetQualifiedSymbol(PositionString name, Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws InvalidIdentifierException;
	
	/**
	 * @see #tryGetQualifiedSymbol(PositionString, Iterator)
	 */
	Symbol tryGetQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException;

	/**
	 * @throws InvalidIdentifierException 
	 * @see #tryGetQualifiedSymbol(PositionString, Iterator)
	 */
	Symbol tryGetQualifiedSymbol(PositionString name) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @see Runtime#getUnqualifiedSymbol(PositionString, SymbolType)
	 * @return null if not found or name was null
	 * @throws InvalidIdentifierException 
	 */
	@InternalMethod
	QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException;
	
	/**
	 * @see #lookup(UnqualifiedSymbol)
	 * @return null if not found or name was null
	 * @throws InvalidIdentifierException 
	 */
	@InternalMethod
	QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException;
	
	/**
	 * Being in a scope, looking downwards for a symbol.
	 * @return null, if symbol was null
	 */
	@InternalMethod
	QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException;

	/**
	 * Being in a scope, looking upwards for a symbol.
	 * @return null, if symbol was null
	 */
	@InternalMethod
	QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol) throws InvalidIdentifierException;
	
}
