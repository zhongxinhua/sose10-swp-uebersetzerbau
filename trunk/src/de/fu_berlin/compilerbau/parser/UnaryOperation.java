package de.fu_berlin.compilerbau.parser;

public class UnaryOperation {
	enum UnaryOperator {
		NOT, PLUS, MINUS, PREINC, PREDEC, POSTINC, POSTDEC
	}
    UnaryOperator operator;
    Expression expr;
}
