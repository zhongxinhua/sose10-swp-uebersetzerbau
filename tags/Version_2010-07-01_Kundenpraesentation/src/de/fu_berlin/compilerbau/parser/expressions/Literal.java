package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein Literal stellt im abstrakten Syntaxbaum einen 
 * elementaren Ausdruck dar. Von ihm erben die konkreten 
 * elementaren Ausdruckknoten.
 * @see FloatLiteral
 * @see IntegerLiteral
 * @see StringLiteral
 * @see NullLiteral
 * @author Markus
 */
@SuppressWarnings("serial")
public abstract class Literal extends Expression {
	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(this);
	}
}