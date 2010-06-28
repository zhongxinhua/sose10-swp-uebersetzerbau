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
public abstract class Expression extends SyntaxTreeNode implements
		StreamPosition {
	public static Expression build(PositionString attributeValue,
			ExpressionType type) {
		return (new StatementParser()).parse(attributeValue, type);
	}

	private int column = 0, line = 0, start = 0;

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getLine() {
		return line;
	}
	
	public void setPosition(int line, int column, int start) {
		this.line = line;
		this.column = column;
		this.start = start;
	}
	
	public void setPosition(StreamPosition pos) {
		setPosition(pos.getLine(), pos.getColumn(), pos.getStart());
	}

	public abstract void printTree(int deep);
}
