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

package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;

/**
 * An identifier was added twice to the same {@link SymbolContainer}.
 * @author kijewski
 */
public class DuplicateIdentifierException extends ContainerSymbolsException {
	
	private static final long serialVersionUID = 8581164664313507022L;
	
	public DuplicateIdentifierException(SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		super(createMessageFor(container, newSymbol, oldSymbol), container, newSymbol, oldSymbol);
	}
	
	public static String createMessageFor(SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		return "In " + container + ": Symbol <" + newSymbol + "> was already introduced as <" +
				oldSymbol + ">";
	}
	
}
