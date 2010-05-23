package de.fu_berlin.compilerbau.parser.expressions;


public class UnaryOperation {
	enum UnaryOperator {
		NOT, PLUS, MINUS, PREINC, PREDEC, POSTINC, POSTDEC
	}
    UnaryOperator operator;
    Expression expr;
}
