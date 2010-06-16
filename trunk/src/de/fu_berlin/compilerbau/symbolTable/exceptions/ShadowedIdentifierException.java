package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;

/**
 * A {@link Symbol} shadows another {@code Symbol}.
 * This exception is only thrown if set so in the {@link Runtime}.
 * @author kijewski
 */
public class ShadowedIdentifierException extends ContainerSymbolsException {
	
	private static final long serialVersionUID = -2661926521450297237L;
	
	public ShadowedIdentifierException(SymbolContainer container, Symbol newSymbol, Symbol oldSymbol) {
		super(createMessageFor(container, newSymbol, oldSymbol), container, newSymbol, oldSymbol);
	}
	
	private static String createMessageFor(SymbolContainer container, Symbol newSymbol, Symbol oldSymbol) {
		return "Symbol <" + newSymbol + "> shadows <" + oldSymbol + "> in " + container;
	}
	
}
