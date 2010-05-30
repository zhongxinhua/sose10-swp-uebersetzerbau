package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * An identifier was added twice to the same {@link SymbolContainer}.
 * @author kijewski
 */
public class DuplicateIdentifierException extends SymbolTableException implements StreamPosition {
	
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

	@Override
	public int getCharacter() {
		return newSymbol.getDeclarationPosition().getCharacter();
	}

	@Override
	public int getLine() {
		return newSymbol.getDeclarationPosition().getLine();
	}

	@Override
	public int getStart() {
		return newSymbol.getDeclarationPosition().getStart();
	}
	
}
