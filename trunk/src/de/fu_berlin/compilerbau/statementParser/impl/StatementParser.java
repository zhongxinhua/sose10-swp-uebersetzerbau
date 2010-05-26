package de.fu_berlin.compilerbau.statementParser.impl;

import java.util.Iterator;

import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.FloatLiteral;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.parser.expressions.IntegerLiteral;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation.BinaryOperator;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation.UnaryOperator;
import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.statementLexer.impl.StatementLexer;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

/* Ausgangsgrammatik:
 * expr -> expr bin_op expr
 *      -> [id|literal|super|this|id[expr]|id(expr)] (.[id|literal|id[expr]|id(expr)])*
 *      -> expr un_post_op
 *      -> un_pre_op expr
 *      -> (expr)
 *      
 * En detail:
 * orterm: 
 *   oterm '||'andterm
 *   andterm
 * andterm:
 *   andterm '&&' bitorterm
 *   bitorterm
 * bitorterm:
 *   bitorterm '|' bitxorterm
 *   bitxorterm
 * bitxorterm:
 *   bitxorterm '^' bitandterm
 *   bitandterm
 * bitandterm:
 *   bitandterm '&' eqterm
 *   eqterm
 * eqterm:
 *   eqterm '==' relterm
 *   eqterm '!=' relterm
 *   relterm
 * relterm:
 *   relterm '<' addterm
 *   relterm '<=' addterm
 *   relterm '>' addterm
 *   relterm '>=' addterm
 *   addterm
 * addterm:
 *   addterm '+' multerm
 *   addterm '-' multerm
 *   multerm
 * multerm:
 *   multerm '*' unarypreterm
 *   multerm '%' unarypreterm
 *   multerm '/' unarypreterm
 *   unarypreterm
 * unarypreterm:
 *   '!'  unarypostterm
 *   '++' unarypostterm
 *   '--' unarypostterm
 *   '+'  unarypostterm
 *   '-'  unarypostterm
 *   'new' unarypostterm
 *   unarypostterm
 * unarypostterm:
 *   unarypostterm '++'
 *   unarypostterm '--'
 *   
 * Sin Chao Bo vo  
 * Mot Suot Thit Cho
 *      
 * */

public class StatementParser {
	private Iterator<StatementNode> tokens;
  private StatementNode current;
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
		return parse(StatementLexer.tokenize(str));
	}
	
	public Expression parse(Iterable<StatementNode> nodes) {
	  tokens = nodes.iterator();
	  next();
	  return expression();
	}
	
	private StatementNode next() {
	  return current = tokens.next();
	}
	
	private Expression expression() {
		return orTerm();
	}
	
	private Expression orTerm() {
	  Expression lhs, rhs;
	  lhs = andTerm();
	  while(current.getType() == TokenType.OR) {
	  	next();
	    rhs = andTerm();
	    lhs = new BinaryOperation(BinaryOperator.OR, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression andTerm() {
		Expression lhs, rhs;
	  lhs = bitOrTerm();
	  while(current.getType() == TokenType.AND) {
	  	next();
	    rhs = bitOrTerm();
	    lhs = new BinaryOperation(BinaryOperator.AND, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression bitOrTerm() {
		Expression lhs, rhs;
	  lhs = bitXorTerm();
	  while(current.getType() == TokenType.BIT_OR) {
	  	next();
	    rhs = bitXorTerm();
	    lhs = new BinaryOperation(BinaryOperator.BITWISE_OR, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression bitXorTerm() {
		Expression lhs, rhs;
	  lhs = bitAndTerm();
	  while(current.getType() == TokenType.BIT_XOR) {
	  	next();
	    rhs = bitAndTerm();
	    lhs = new BinaryOperation(BinaryOperator.BITWISE_XOR, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression bitAndTerm() {
		Expression lhs, rhs;
	  lhs = eqTerm();
	  while(current.getType() == TokenType.BIT_AND) {
	  	next();
	    rhs = eqTerm();
	    lhs = new BinaryOperation(BinaryOperator.BITWISE_AND, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression eqTerm() {
		Expression lhs, rhs;
	  lhs = relTerm();
	  Loop:
	  while(true) {
	  	BinaryOperator op;
	  	switch(current.getType()) {
	  		case EQ: op = BinaryOperator.EQUAL;	   break;
	  		case NE: op = BinaryOperator.NOTEQUAL; break;
	  		default: break Loop;
	  	}
	  	next();
	    rhs = relTerm();
	    lhs = new BinaryOperation(op, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression relTerm() {
		Expression lhs, rhs;
	  lhs = addTerm();
	  Loop:
	  while(true) {
	  	BinaryOperator op;
	  	switch(current.getType()) {
	  		case LT: op = BinaryOperator.LESS_THAN;	    break;
	  		case GT: op = BinaryOperator.GREATER_THAN;  break;
	  		case LE: op = BinaryOperator.LESS_EQUAL;    break;
	  		case GE: op = BinaryOperator.GREATER_EQUAL; break;
	  		default: break Loop;
	  	}
	  	next();
	    rhs = addTerm();
	    lhs = new BinaryOperation(op, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression addTerm() {
		Expression lhs, rhs;
	  lhs = mulTerm();
	  Loop:
	  while(true) {
	  	BinaryOperator op;
	  	switch(current.getType()) {
	  		case PLUS:  op = BinaryOperator.ADD;	 break;
	  	  case MINUS: op = BinaryOperator.SUBSTRACT; break;
	  	  default: break Loop;
	  	}
	  	next();
	    rhs = mulTerm();
	    lhs = new BinaryOperation(op, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression mulTerm() {
		Expression lhs, rhs;
	  lhs = unaryPrefixTerm();
	  Loop:
	  while(true) {
	  	BinaryOperator op;
	  	switch(current.getType()) {
	  		case TIMES:   op = BinaryOperator.TIMES;	 break;
	  		case DIVIDES: op = BinaryOperator.DIVIDES; break;
	  		case MOD:     op = BinaryOperator.MODULOS;	   break;
	  	  default: break Loop;
	  	}
	  	next();
	    rhs = unaryPrefixTerm();
	    lhs = new BinaryOperation(op, lhs, rhs);
	  }
	  return lhs;
	}
	
	private Expression unaryPrefixTerm() {
	  UnaryOperator op = null;
	  switch(current.getType()) {
			case NOT:   op = UnaryOperator.NOT;	 break;
			case INCR:  op = UnaryOperator.PREINC; break;
			case DECR:  op = UnaryOperator.PREDEC;	   break;
			case MINUS: op = UnaryOperator.MINUS;	   break;
			case PLUS:  op = UnaryOperator.PLUS;	   break;
			//case NEW:   op = UnaryOperator.MOD;	   break;
			//TODO: spezialfall NEW behandeln
		}
	  if(op!=null)
	    next();
	  Expression expr = unaryPostfixTerm();
	  if(op!=null)
	  	expr = new UnaryOperation(op, expr);
	  return expr;
	}

	private Expression unaryPostfixTerm() {
		UnaryOperator op = null;
		Expression expr = factor();
	  switch(current.getType()) {
			case INCR:  op = UnaryOperator.POSTINC; break;
			case DECR:  op = UnaryOperator.POSTDEC;	   break;
		}
	  if(op!=null) {
	    next();
	  	expr = new UnaryOperation(op, expr);
	  }
	  return expr;
	}
	
	private Expression factor() {
		Expression result = null;
		switch(current.getType()) {
		  case STRING:
		  	try {
					result = new StringLiteral(current.getString());
				} catch (IllegalAccessException e) {}
				next();
		  	break;
			case NULL:       //Null
				result = NullLiteral.NULL;
				next();
				break;
			case ID:         //Bezeichner
				try {
					result = new Identifier(current.getString());
				} catch (IllegalAccessException e) {}
				next();
				break; 
			case FLOAT:      //Flieﬂkomma
				try {
					result = new FloatLiteral(current.getNumber());
				} catch (IllegalAccessException e) {}
				next();
				break;
			case INT:        //Ganzzahl
				try {
					result = new IntegerLiteral(current.getNumber());
				} catch (IllegalAccessException e) {}
				next();
				break;
			case PAREN_OPEN: //Unterausdruck
				next();
				result = expression();
				if(current.getType()!=TokenType.PARENT_CLOSE)
					ErrorHandler.error(null, "missing ')'");
				else
				  next();
				break;
		}
		return result;
	}

  public static void main(String[] args) {
		String[] tests = new String[] {
				"\"abc\" - abc - 123 - 1.23"
			//"--a++ + !b <= 1.0"
			//"a+b*2",
			//"1+2-3*4/5%6",
			//"1 & 2 | 3 ^ 4",
			//"a && b || c"
		};
		for(String str : tests ) {
			System.out.println("== testing \""+str+"\" ===");
			Expression expr = new StatementParser().parse(str);
			expr.printTree(0);
		}
	}
}
