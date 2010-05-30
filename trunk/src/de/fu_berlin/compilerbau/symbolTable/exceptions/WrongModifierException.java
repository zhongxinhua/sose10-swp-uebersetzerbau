package de.fu_berlin.compilerbau.symbolTable.exceptions;

public class WrongModifierException extends SymbolTableException {
	
	private static final long serialVersionUID = 8414796337183141083L;

	public WrongModifierException(String message) {
		super(message);
	}
	
}
