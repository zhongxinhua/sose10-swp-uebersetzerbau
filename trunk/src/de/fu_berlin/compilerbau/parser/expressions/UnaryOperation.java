package de.fu_berlin.compilerbau.parser.expressions;

/**
 * UnaryOperation stellt im abstrakten Syntaxbaum einen 
 * Ausdruck dar auf dem eine unÃ¤re Operation ausgefÃ¼hrt wird.
 * @author Markus
 */
public class UnaryOperation extends Expression {
	public enum UnaryOperator {
		NOT, PLUS, MINUS, PREINC, PREDEC, POSTINC, POSTDEC
	}
	
	private UnaryOperator operator;
    private Expression expression;
    
    public UnaryOperation(UnaryOperator op, Expression arg) {
		operator = op;
		expression = arg;
	}
    
	@Override
	public Type getType() {
		return expression.getType();
	}

	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(operator);
		expression.printTree(deep+1);
	}
	//BEGIN get-Methoden für Builder
	public UnaryOperator getUnaryOperator(){
		return operator;
	}
	public Expression getExpression(){
		return expression;
	}
	//END get-Methoden für Builder
}
