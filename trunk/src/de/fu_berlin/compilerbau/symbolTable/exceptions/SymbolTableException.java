package de.fu_berlin.compilerbau.symbolTable.exceptions;


public class SymbolTableException extends Exception {
	
	private static final long serialVersionUID = -3810445745344223529L;

	public SymbolTableException() {
		super();
	}
	
	public SymbolTableException(String message) {
		super(message);
	}
	
	public SymbolTableException(Throwable cause) {
		super(cause);
	}
	
	public SymbolTableException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
