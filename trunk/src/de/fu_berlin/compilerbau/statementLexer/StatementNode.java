package de.fu_berlin.compilerbau.statementLexer;

public class StatementNode extends StreamPosition {
	
	TokenType getType();
	
	/**
	 * result will be an instance of for specific TokenType:
	 * <ul>
	 *   <li>ID, STRING: CharSequence</li>
	 *   <li>INT, REAL: Number</li>
	 *   <li>otherwise: null</li>
	 * </ul>
	 */
	Object getValue();
	
}
