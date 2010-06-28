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

import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;

/**
 * Helper interface regarding the {@link UnqualifiedSymbol UnqualifiedSymbols}.
 * 
 * You most likely won't need to interact with this interface directly.
 * @author rene
 */
public interface UnqualifiedSymbolsMap<U extends UnqualifiedSymbol> {
	
	public static enum ReplaceFunResult {
		REPLACED,
		NOT_REPLACED
	}

	public static interface ReplaceFunc {
		
		/**
		 * @return if the symbol could be replaced
		 */
		public ReplaceFunResult replace() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
		
	}
	
	public void addUnqualifiedSymbol(U unqualifiedSymbol, ReplaceFunc replacer);
	
	/**
	 * @see Runtime#qualifyAllSymbols()
	 */
	public Set<UnqualifiedSymbol> qualifyAllSymbols() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	boolean hasUnqualifiedSymbols();

	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols();

}
