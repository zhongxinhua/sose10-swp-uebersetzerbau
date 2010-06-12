package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;

/**
 * An identifier was added twice to the same {@link SymbolContainer}.
 * @author kijewski
 */
public class DuplicateIdentifierException extends SymbolTableException {
	
	private static final long serialVersionUID = 8581164664313507022L;
	
	protected final SymbolContainer container;
	protected final Symbol newSymbol;
	protected final Symbol oldSymbol;
	
	public DuplicateIdentifierException(SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		super(createMessageFor(container, newSymbol, oldSymbol));
		this.container = container;
		this.newSymbol = newSymbol;
		this.oldSymbol = oldSymbol;
	}
	
	private static String createMessageFor(SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		return "In " + container + ": Symbol <" + newSymbol + "> was already introduced as <" +
				oldSymbol + ">";
	}
	
	public SymbolContainer getContainer() {
		return container;
	}

	
	public Symbol getNewSymbol() {
		return newSymbol;
	}

	
	public Symbol getOldSymbol() {
		return oldSymbol;
	}
	
}
