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
	
	private static String createMessageFor(SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		return "In " + container + ": Symbol <" + newSymbol + "> was already introduced as <" +
				oldSymbol + ">";
	}
	
}
