package de.fu_berlin.compilerbau.statementParser.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.IntegerLiteral;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.RealLiteral;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.statementLexer.impl.StatementLexer;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class StatementParser {
	private static class Operator {
		TokenType operator;
		boolean leftAssociative;
		int priority;
	}
	
	private static final Operator[] precTable = new Operator[] {
		new Operator() { {operator = TokenType.DOT;           priority = 140; leftAssociative = true;} },
		new Operator() { {operator = TokenType.PAREN_OPEN;    priority = 130; leftAssociative = true;} },
		new Operator() { {operator = TokenType.PARENT_CLOSE;  priority = 130; leftAssociative = true;} },
		new Operator() { {operator = TokenType.BRACKET_OPEN;  priority = 130; leftAssociative = true;} },
		new Operator() { {operator = TokenType.BRACKET_CLOSE; priority = 130; leftAssociative = true;} },
		new Operator() { {operator = TokenType.INCR;          priority = 130; leftAssociative = false;} },
		new Operator() { {operator = TokenType.DECR;          priority = 130; leftAssociative = false;} },
		new Operator() { {operator = TokenType.INCR;          priority = 120; leftAssociative = true;} },
		new Operator() { {operator = TokenType.DECR;          priority = 120; leftAssociative = true;} },
		new Operator() { {operator = TokenType.NOT;           priority = 120; leftAssociative = true;} },
		new Operator() { {operator = TokenType.NEW;           priority = 120; leftAssociative = true;} },
		new Operator() { {operator = TokenType.TIMES;         priority = 110; leftAssociative = true;} },
		new Operator() { {operator = TokenType.SLASH;         priority = 110; leftAssociative = true;} },
		new Operator() { {operator = TokenType.MOD;           priority = 110; leftAssociative = true;} },
		new Operator() { {operator = TokenType.PLUS;          priority = 100; leftAssociative = true;} },
		new Operator() { {operator = TokenType.MINUS;         priority = 100; leftAssociative = true;} },
		new Operator() { {operator = TokenType.LE;            priority = 90;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.GE;            priority = 90;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.LT;            priority = 90;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.GT;            priority = 90;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.NE;            priority = 80;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.EQ;            priority = 80;  leftAssociative = true;} },
		//new Operator() { {operator = TokenType.ASSIGN;        priority = 70;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.BIT_AND;       priority = 60;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.BIT_XOR;       priority = 50;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.BIT_OR;        priority = 40;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.AND;           priority = 30;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.OR;            priority = 20;  leftAssociative = true;} },
		new Operator() { {operator = TokenType.COMMA;         priority = 10;  leftAssociative = true;} }
	};
	
	public static Expression parse(String str) {
		return parse(new PositionString(str, new StreamPosition() {
			private static final long serialVersionUID = 1L;
			public int getStart() {	return 1; }
			public int getLine() { return 1; }
			public int getCharacter() {	return 0; }
		}));
	}
	
	public static Expression parse(PositionString str) {
		Reader reader = new StringReader(str.toString());
		PositionCharacterStream stream = new PositionCharacterStream(reader, str);
		return parse(StatementLexer.tokenize(stream));
	}
	
	public static Expression parse(Iterable<StatementNode> nodes) {
		Deque<Expression> outputStack = new LinkedList<Expression>();
		Stack<TokenType> operatorStack   = new Stack<TokenType>();
		
		for(StatementNode node: nodes) {
			TokenType type = node.getType();
			switch(type) {
				case DOT:
				case PAREN_OPEN:
				case PARENT_CLOSE:
				case BRACKET_OPEN:
				case BRACKET_CLOSE:
				case INCR:
				case DECR:
				case NOT:
				case NEW:					
				case TIMES:
				case SLASH:
				case MOD:
				case PLUS:
				case MINUS:
				case LE:
				case GE:	
				case LT:
				case GT:
				case NE:
				case EQ:
				//case ASSIGN:
				case BIT_AND:
				case BIT_XOR:
				case BIT_OR:
				case AND:
				case OR:
				case COMMA:
					operatorStack.add(type);
					break;

				case NULL:
					outputStack.add(NullLiteral.NULL);
					break;
				case ID:
					//outputStack.add((CharSequence) node.getValue());
					break;
				case INT:
					try {
						outputStack.add(new IntegerLiteral((Number) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
				case REAL:
					try {
						outputStack.add(new RealLiteral((Number) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
				case STRING:
					try {
						outputStack.add(new StringLiteral((CharSequence) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
					
				//case ERROR:
				default:
					ErrorHandler.error(null, "unknown state...");
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		
	}
}
