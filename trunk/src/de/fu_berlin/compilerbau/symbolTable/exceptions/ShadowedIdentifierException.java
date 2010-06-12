package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;

/**
 * A {@link Symbol} shadows another {@code Symbol}.
 * This exception is only thrown if set so in the {@link Runtime}.
 * @author kijewski
 */
public class ShadowedIdentifierException extends SymbolTableException {
	
	private static final long serialVersionUID = -2661926521450297237L;
	
	protected final Symbol newSymbol;
	protected final Symbol oldSymbol;
	
	public ShadowedIdentifierException(Symbol newSymbol, Symbol oldSymbol) {
		super(createMessageFor(newSymbol, oldSymbol));
		this.newSymbol = newSymbol;
		this.oldSymbol = oldSymbol;
	}
	
	private static String createMessageFor(Symbol newSymbol, Symbol oldSymbol) {
		return "Symbol <" + newSymbol + "> shadows <" + oldSymbol + ">";
	}

	
	public Symbol getNewSymbol() {
		return newSymbol;
	}

	
	public Symbol getOldSymbol() {
		return oldSymbol;
	}
	
}
