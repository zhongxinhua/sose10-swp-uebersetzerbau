package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;

/**
 * @author kijewski
 */
public abstract class ContainerSymbolsException extends SymbolTableException {
	
	private static final long serialVersionUID = -3854903085964492800L;
	
	protected final SymbolContainer container;
	protected final Symbol newSymbol;
	protected final Symbol oldSymbol;
	
	public ContainerSymbolsException(String msg, SymbolContainer container, Symbol newSymbol,
			Symbol oldSymbol) {
		super(msg);
		this.container = container;
		this.newSymbol = newSymbol;
		this.oldSymbol = oldSymbol;
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
