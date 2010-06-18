package de.fu_berlin.compilerbau.symbolTable.exceptions;

import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.util.PositionString;

public class InvalidIdentifierException extends SymbolTableException {
	
	private static final long serialVersionUID = -2790725272527032269L;
	
	protected final SymbolContainer container;
	protected final PositionString name;

	public InvalidIdentifierException(SymbolContainer container, PositionString name) {
		super(createMessageFor(container, name));
		this.container = container;
		this.name = name;
	}

	public static String createMessageFor(SymbolContainer container, PositionString name) {
		return "Invalid name " + name + " occured in " + container + ".";
	}
	
	public SymbolContainer getContainer() {
		return container;
	}

	public PositionString getName() {
		return name;
	}

}
