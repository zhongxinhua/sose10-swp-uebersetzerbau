package de.fu_berlin.compilerbau.parser.expressions;

/**
 * BinaryOperation stellt im abstrakten Syntaxbaum Ausdrücke dar, die zwei Unterausdrücke mit 
 * einer binären Operation verbindet. 
 * @author Markus
 */
public class BinaryOperation extends Expression { // a+b
	public enum BinaryOperator {
		ADD, SUBSTRACT, TIMES, DIVIDES, MODULOS, GREATER_THAN, LESS_THAN, GREATER_EQUAL,
		LESS_EQUAL, NOTEQUAL, EQUAL, AND, OR, BITWISE_XOR, BITWISE_AND, BITWISE_OR
	}
	
	private BinaryOperator operator;
	private Expression left;
	private Expression right;
	
	public BinaryOperation(BinaryOperator op, Expression lhs, Expression rhs) {
		operator = op;
		left     = lhs;
		right    = rhs;
	}

	@Override
	public void printTree(int deep) {
		int newDeep = deep+1;
		left.printTree(newDeep);	
		
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(operator);
		
		right.printTree(newDeep);
	}
	
	@Override
	public String toString() {
		return left.toString()+" "+operator.toString()+" "+right.toString();
	}
	//BEGIN get-Methoden f�r Builder
	public Expression getLeftExpression(){
		return left;
	}
	public BinaryOperator getBinaryOperator(){
		return operator;
	}
	public Expression getRightExpression(){
		return right;
	}
	//END get-Methoden f�r Builder
}