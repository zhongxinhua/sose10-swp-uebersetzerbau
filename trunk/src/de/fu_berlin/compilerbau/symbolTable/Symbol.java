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

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * Base interface of all symbols.
 * 
 * <p/>Don't you dare to use <code>instanceof</code> for testing the actual type of a symbol!
 * See {@link #hasType(SymbolType)}.
 * @author rene
 */
public interface Symbol extends Comparable<Symbol> {
	
	/**
	 * Symbols of different {@link Runtime runtimes} may not be combined.
	 * The effects would be mostly random.
	 */
	Runtime getRuntime();
	
	/**
	 * Where and by whom was this symbol mentioned. (For circular dependencies.)
	 * @return immutable list [(who,where)]
	 */
	Set<Map.Entry<Symbol,StreamPosition>> getMentions();
	
	/**
	 * Somewhere this symbol was accessed.
	 * @param who The {@link SymbolContainer} that referenced the symbol.
	 * @param where Where in the data stream the symbol was mentioned.
	 */
	void addMention(Symbol who,StreamPosition where);
	
	/**
	 * Immediately parent of this symbol.
	 * @return null if this is an {@link UnqualifiedSymbol unqualified symbol} or
	 * 	the {@link Runtime}.
	 */
	SymbolContainer getParent();
	
	/**
	 * return <code>leftType implicates (({@link QualifiedSymbol})this).getType())</code>
	 * 
	 * <p/>E.g. {@link SymbolType.CONSTRUCTOR} → {@link SymbolType.METHOD} → {@link SymbolType.SCOPE}
	 * 
	 * <p/>You must not use <code>instanceof</code> to determine the type of a Symbol!
	 * @return null if this is a {@link QualifiedSymbol}
	 */
	Boolean hasType(SymbolType leftType);
	
}
