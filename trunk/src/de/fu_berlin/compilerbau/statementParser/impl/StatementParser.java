package de.fu_berlin.compilerbau.statementParser.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.parser.expressions.IntegerLiteral;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.FloatLiteral;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation.BinaryOperator;
import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.statementLexer.impl.StatementLexer;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class StatementParser {
	private PrecedenceTable ptable = PrecedenceTable.getInstance();
	private Deque<Expression> outputStack  = new LinkedList<Expression>();
	private Stack<TokenType> operatorStack = new Stack<TokenType>();	
	
	public StatementParser() {}
	
	public Expression parse(String str) {
		return parse(new PositionString(str, new StreamPosition() {
			private static final long serialVersionUID = 1L;
			public int getStart() {	return 1; }
			public int getLine() { return 1; }
			public int getCharacter() {	return 0; }
		}));
	}
	
	public Expression parse(PositionString str) {
		Reader reader = new StringReader(str.toString());
		PositionCharacterStream stream = new PositionCharacterStream(reader, str);
		return parse(StatementLexer.tokenize(stream));
	}
	
	public Expression parse(Iterable<StatementNode> nodes) {		
		//* While there are tokens to be read
		Iterator<StatementNode> it = nodes.iterator();
		while(it.hasNext()) {
	        //* Read a token ==> node
			StatementNode node = it.next();
			TokenType type = node.getType();
			System.out.println(type);
			switch(type) {
			    //* If the token is a number, then add it to the output queue.
				case NULL: outputStack.add(NullLiteral.NULL); break;
				case INT:
					try {
						outputStack.add(new IntegerLiteral((Number) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
				case REAL:
					try {
						outputStack.add(new FloatLiteral((Number) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
				case STRING:
					try {
						outputStack.add(new StringLiteral((CharSequence) node.getValue()));
					} catch (IllegalAccessException e) {}
					break;
					
				//* If the token is a function token, then push it onto the stack.
				case ID: 
					try {
						outputStack.add(new Identifier((CharSequence) node.getValue()));
					} catch (IllegalAccessException e1) {} 
					break;
			
				//* If the token is a function argument separator (e.g., a comma):
				case COMMA:
					//* Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue. If no left parentheses are encountered, either the separator was misplaced or parentheses were mismatched.
					break;
					
				
				//* If the token is a left parenthesis, then push it onto the stack.
				case PAREN_OPEN:
				case BRACKET_OPEN:

				/*
		         * If the token is a right parenthesis:

		            * Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
		            * Pop the left parenthesis from the stack, but not onto the output queue.
		            * If the token at the top of the stack is a function token, pop it onto the output queue.
		            * If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
		        */
				case PARENT_CLOSE:
				case BRACKET_CLOSE:
					
				case DOT:
					break;
					
				default:
					//* If the token is an operator, o1, then:
					if(ptable.isOperator(type)) {					
						TokenType o1 = type;
						//* while there is an operator token, o2, at the top of the stack, and
						while(!operatorStack.empty()) {
							TokenType o2 = operatorStack.peek();
							//either o1 is left-associative and its precedence is less than or equal to that of o2,
							if((ptable.isLeftAssociative(o1) && ptable.compareOperators(o1,o2)<=0) ||
							//or o1 is right-associative and its precedence is less than that of o2,
								(!ptable.isLeftAssociative(o1) && ptable.compareOperators(o1,o2)<0)) {
								//pop o2 off the stack, onto the output queue;
								operatorStack.pop();
								Expression expr2 = outputStack.pollLast();
								Expression expr1 = outputStack.pollLast();
								reduce(o2, expr1, expr2);
							} else
								break;
						}
						//* push o1 onto the stack.
						operatorStack.push(o1);
						break;	
					} else
						ErrorHandler.error(null, "unknown state... ("+type.toString()+")");
			}
		}
		
		//* When there are no more tokens to read:
	    //* While there are still operator tokens in the stack:
		while(!operatorStack.empty()) {
			TokenType operator = operatorStack.pop();
		    //* If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
			//TODO check for bad parenthesis
			
			//* Pop the operator onto the output queue.
			Expression expr2 = outputStack.pollLast();
			Expression expr1 = outputStack.pollLast();
			reduce(operator, expr1, expr2);
		}
		
		//* Exit.
		return outputStack.getFirst();
	}
	
	private void reduce(TokenType type, Expression expr1, Expression expr2) {
		if(expr1 != null) {
			BinaryOperator op = null;
			switch(type) {
			//ADD, MINUS, MUL, DIV, MOD, GREATERTHAN, LESSTHAN, GREATEREQUAL,
			//LESSEQUAL, NOTEQUAL, EQUAL, AND, OR, XOR
				case PLUS: op = BinaryOperator.ADD; break;
				case MINUS: op = BinaryOperator.MINUS; break;
				case TIMES: op = BinaryOperator.MUL; break;
				case DIVIDES: op = BinaryOperator.DIV; break;
				case MOD: op = BinaryOperator.MOD; break;
				case GT: op = BinaryOperator.GREATER_THAN; break;
				case LT: op = BinaryOperator.LESS_THAN; break;
				case GE: op = BinaryOperator.GREATER_EQUAL; break;
				case LE: op = BinaryOperator.LESS_EQUAL; break;
				case NE: op = BinaryOperator.NOTEQUAL; break;
				case EQ: op = BinaryOperator.EQUAL; break;
				case AND: op = BinaryOperator.AND; break;
				case OR: op = BinaryOperator.OR; break;
				case BIT_AND: op = BinaryOperator.BITWISE_AND; break;
				case BIT_OR: op = BinaryOperator.BITWISE_OR; break;
				case BIT_XOR: op = BinaryOperator.BITWISE_XOR; break;
				default:
					ErrorHandler.error(null, type.toString()+" needs a handler");
			}
			if(op!=null) {
				Expression result = new BinaryOperation(op, expr1, expr2);
				outputStack.add(result);
			}
		} else
			ErrorHandler.error(null, "what to do? so it wasnt you?");
	}
	
	public static void main(String[] args) {
		String[] tests = new String[] {
			//"a+b*2",
			//"1+2-3*4/5%6",
			//"1 & 2 | 3 ^ 4",
			"a && b || c"
		};
		for(String str : tests ) {
			System.out.println("== testing \""+str+"\" ===");
			Expression expr = new StatementParser().parse(str);
			expr.printTree(0);
		}
	}
}
