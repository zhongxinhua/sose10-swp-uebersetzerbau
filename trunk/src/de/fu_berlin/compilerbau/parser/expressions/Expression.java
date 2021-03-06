package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.parser.SyntaxTreeNode;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * Eine Expression stellt im abstrakten Syntaxbaum einen Ausdruck dar.
 * 
 * @see ArrayAccess
 * @see BinaryOperation
 * @see Literal
 * @see FunctionCall
 * @see Identifier
 * @see MemberAccess
 * @see ObjectCreation
 * @see UnaryOperation
 */
@SuppressWarnings("serial")
public abstract class Expression extends SyntaxTreeNode {
	public static Expression build(PositionString attributeValue,
			ExpressionType type) {
		return (new StatementParser()).parse(attributeValue, type);
	}

	public abstract void printTree(int deep);
}
