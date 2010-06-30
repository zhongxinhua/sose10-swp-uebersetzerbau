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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunResult;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunc;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.PositionString;

class ShadowedSymbols {
	
	public final Map<QualifiedSymbol, Set<Symbol>> list = new TreeMap<QualifiedSymbol, Set<Symbol>>();
	protected final Map<String, Set<Symbol>> names = new HashMap<String, Set<Symbol>>();
	protected final SymbolContainerImpl container;
	
	public ShadowedSymbols(SymbolContainerImpl container) {
		this.container = container;
	}

	private static final long serialVersionUID = -8115798161952915022L;
	
	private Set<Symbol> put(QualifiedSymbol newSymbol, final Symbol oldSymbol) {
		String name = newSymbol.getName();
		final Set<Symbol> result_ = names.get(name);
		final Set<Symbol> result;
		if(result_ != null) {
			result = result_;
		} else {
			result = new TreeSet<Symbol>();
			list.put(newSymbol, result);
			names.put(name, result);
		}
		result.add(oldSymbol);
		if(oldSymbol.hasType(SymbolType.VOID) == null) {
			ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
				
				@Override
				public ReplaceFunResult replace()
						throws DuplicateIdentifierException,
						ShadowedIdentifierException,
						WrongModifierException,
						InvalidIdentifierException {
					final QualifiedSymbol qualifiedSymbol = ((UnqualifiedSymbol)oldSymbol).qualify();
					if(qualifiedSymbol != null) {
						result.remove(oldSymbol);
						result.add(qualifiedSymbol);
						return ReplaceFunResult.REPLACED;
					} else {
						return ReplaceFunResult.NOT_REPLACED;
					}
				}
				
			};
			newSymbol.getRuntime().getUnqualifiedSymbolsMap().addUnqualifiedSymbol((UnqualifiedSymbol) oldSymbol, replaceFunc);
		}
		return result;
	}
	
	public void test(PositionString name, QualifiedSymbol newSymbol) throws ShadowedIdentifierException, InvalidIdentifierException {
		final Runtime rt = container.getRuntime();
		Symbol shadowed = container.lookTreeUp(rt.getUnqualifiedSymbol(name, container));
		if(shadowed != null) {
			if(shadowed.hasType(SymbolType.SET_OF_METHODS)) {
				// TODO: Parameter überprüfen
				return;
			}
			
			if(rt.getThrowsAtShadowing()) {
				throw new ShadowedIdentifierException(container, newSymbol, shadowed);
			}
			put(newSymbol, shadowed);
			rt.getAllShadowsList().add(new Pair<QualifiedSymbol, Symbol>(newSymbol, shadowed));
		}
	}

}
