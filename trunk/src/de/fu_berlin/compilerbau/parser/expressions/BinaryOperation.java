package de.fu_berlin.compilerbau.parser.expressions;


public class BinaryOperation extends Expression { // a+b
	public enum BinaryOperator {
		ADD, MINUS, MUL, DIV, MOD, GREATER_THAN, LESS_THAN, GREATER_EQUAL,
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
	public Type getType() {
		// TODO type checking
		return null;
	}

	@Override
	public void printTree(int deep) {
		int newDeep = deep+1;
		left.printTree(newDeep);	
		
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(operator);
		
		right.printTree(newDeep);
	}
}