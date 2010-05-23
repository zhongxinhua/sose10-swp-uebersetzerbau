package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.parser.Type;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * * <b>*********** Not implemented yet! ***********</b>
 * <p/>
 * <ul>
 * <li>{@link FunctionCall}</li>
 * <li>{@link BinaryOperation}</li>
 * <li>{@link UnaryOperation}</li>
 * <li>{@link Literal}</li>
 * </ul>
 * 
 */
public abstract class Expression {

	public static Expression build(PositionString attributeValue) {
		return StatementParser.parse(attributeValue);
	}

	public abstract Type getType();
}
