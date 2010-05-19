package de.fu_berlin.compilerbau.statementLexer;

import de.fu_berlin.compilerbau.util.StreamPosition;

public interface StatementNode extends StreamPosition {
	
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
	
	/**
	 * @return same as {@link #getType()}
	 * @throws IllegalAccessException if invalid type
	 */
	Number getNumber() throws IllegalAccessException;
	
	/**
	 * @return same as {@link #getType()}
	 * @throws IllegalAccessException if invalid type
	 */
	CharSequence getString() throws IllegalAccessException;
	
}
