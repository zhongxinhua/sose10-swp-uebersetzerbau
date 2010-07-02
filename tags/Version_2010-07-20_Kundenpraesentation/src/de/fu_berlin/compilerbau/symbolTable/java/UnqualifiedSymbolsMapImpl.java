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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunc;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;

class UnqualifiedSymbolsMapImpl<U extends UnqualifiedSymbol>
		extends TreeMap<UnqualifiedSymbol,List<ReplaceFunc>>
		implements UnqualifiedSymbolsMap<U> {
	
	private static final long serialVersionUID = 1730399893229653855L;
	
	public void addUnqualifiedSymbol(U unqualifiedSymbol, ReplaceFunc replacer) {
		List<UnqualifiedSymbolsMapImpl.ReplaceFunc> list = get(unqualifiedSymbol);
		if(list == null) {
			list = new LinkedList<UnqualifiedSymbolsMapImpl.ReplaceFunc>();
			put(unqualifiedSymbol, list);
		}
		list.add(replacer);
	}
	
	public Set<UnqualifiedSymbol> qualifyAllSymbols()
			throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException {
		
		final Iterator<Map.Entry<UnqualifiedSymbol, List<ReplaceFunc>>> symbolsIterator =
				entrySet().iterator();
		while(symbolsIterator.hasNext()) {
			final Map.Entry<UnqualifiedSymbol, List<ReplaceFunc>> symbolAndReplacers = symbolsIterator.next();
			final List<ReplaceFunc> replacers = symbolAndReplacers.getValue();
			final Iterator<ReplaceFunc> replacersIterator = replacers.iterator();
			while(replacersIterator.hasNext()) {
				final ReplaceFunc replacer = replacersIterator.next();
				if(replacer.replace() == ReplaceFunResult.REPLACED) {
					replacersIterator.remove();
				}
			}
			if(replacers.isEmpty()) {
				symbolsIterator.remove();
			}
		}
		return keySet();
		
	}

	@Override
	public boolean hasUnqualifiedSymbols() {
		return !isEmpty();
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		return keySet();
	}

}
