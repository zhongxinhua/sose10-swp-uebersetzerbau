package de.fu_berlin.compilerbau.statementParser.impl;

import java.util.*;

import de.fu_berlin.compilerbau.parser.expressions.*;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation.BinaryOperator;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation.UnaryOperator;
import de.fu_berlin.compilerbau.statementLexer.*;
import de.fu_berlin.compilerbau.statementLexer.impl.*;
import de.fu_berlin.compilerbau.util.*;

/* Ausgangsgrammatik:
 * expr -> expr bin_op expr
 *      -> [id|literal|super|this|id[expr]|id(expr)] (.[id|literal|id[expr]|id(expr)])*
 *      -> expr un_post_op
 *      -> un_pre_op expr
 *      -> (expr)
 *      
 * En detail:
 * expression:
 *   orterm
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
 *   factor '++'
 *   factor '--'
 * factor:
 *   id
 *   int
 *   float
 *   string
 *   (expression)
 *   
 *     
 * Vietnamesische Sprüche:    
 *   - Sin Chao Bo vo     (Hallo Vater meiner Frau)  
 *   - Mot Suot Thit Cho  (Eine Portion Hundefleisch!)
 * */

/**
 * Diese Klasse verarbeitet den Token-Strom des Statement-Lexer weiter und
 * produziert einen Syntaxbaum für den enthaltenen Ausdruck.
 */
public class StatementParser {
	private Iterator<StatementNode> tokens;
	private StatementNode current;
	
	/**
	 * Dieser Typ dient als Auswahlparameter für den Parser, 
	 * da dieser verschiedene Ausdrücke parst. 
	 * @author Markus
	 */
	public enum ExpressionType {
		RVALUE,
		LVALUE,
		FUNCTIONCALL
	}

	public StatementParser() {
	}

	/**
	 * siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 * Diese Methode wird nur zum Testen verwendet!
	 * @param str
	 *            ein String, der einen Ausdruck enthält
	 * @param type
	 *            siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 * @return siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 */
	public Expression parse(String str, ExpressionType type) {
		return parse(new PositionString(str, new StreamPosition() {
			private static final long serialVersionUID = 1L;

			public int getStart() {
				return 1;
			}

			public int getLine() {
				return 1;
			}

			public int getCharacter() {
				return 0;
			}
		}), type);
	}

	/**
	 * siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 * 
	 * @param str
	 *            ein PositionString, der einen Ausdruck enthält
	 * @param type
	 *            siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 * @return siehe parse(Iterable&lt;StatementNode&gt; nodes)
	 */
	public Expression parse(PositionString str, ExpressionType type) {
		return parse(StatementLexer.tokenize(str), type);
	}

	/**
	 * parst einen Token-Strom des StatementLexer und generiert einen Syntaxbaum
	 * für den mit den Tokens gegebenen Ausdruck.
	 * 
	 * @param nodes
	 *            ein Token-Strom des StatementLexer
	 * @param type
	 *            gibt an was für ein Unterausdruck geparst werden soll
	 * @return gibt einen Syntaxbaum wieder, der den Ausdruck repräsentiert
	 */
	public Expression parse(Iterable<StatementNode> nodes, ExpressionType type) {
		tokens = nodes.iterator();
		next();
		Expression result;
		switch(type) {
		case LVALUE:
			result = factor();
			if(!isLValue(result))
				ErrorHandler.error(null, "the expression is not a valid lvalue!");
			return result;
		case FUNCTIONCALL:
			result = factor();
			if(!isFunctionCall(result))
				ErrorHandler.error(null, "the expression is not a valid function call!");
			return result;
		default: //RVALUE
		  return expression();
		}
	}

	/**
	 * überprüft ob ein gegebener Audruck ein L-Wert ist.
	 * @param expr
	 * @return true wenn der Ausdruck ein gültiger L-Wert ist, sonst false
	 */
	private boolean isLValue(Expression expr) {
		//letztes Element in der Kette muss ein Bezeichner oder 
		//ein Array-Zugriff sein!
		while(expr instanceof MemberAccess)
			expr = ((MemberAccess) expr).getChild();
		return expr instanceof ArrayAccess ||
		       expr instanceof Identifier;
	}
	
	/**
	 * überprüft ob ein gegebener Audruck ein Funktionsaufruf ist.
	 * @param expr
	 * @return true wenn der Ausdruck ein gültiger L-Wert ist, sonst false
	 */
	private boolean isFunctionCall(Expression expr) {
		//letztes Element in der Kette muss ein Funktionsaufruf
		while(expr instanceof MemberAccess)
			expr = ((MemberAccess) expr).getChild();
		return expr instanceof FunctionCall;
	}

	/**
	 * liefert den nächsten Token iom Token-Strom und speichert diesen in einer
	 * lokalen Variable
	 * 
	 * @return gibt den aktuellen Token zurück
	 */
	private StatementNode next() {
		return current = tokens.next();
	}

	/**
	 * parst einen Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression expression() {
		return orTerm();
	}

	/**
	 * parst einen '||'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression orTerm() {
		Expression lhs, rhs;
		lhs = andTerm();
		while (current.getType() == TokenType.OR) {
			next();
			rhs = andTerm();
			lhs = new BinaryOperation(BinaryOperator.OR, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '&&'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression andTerm() {
		Expression lhs, rhs;
		lhs = bitOrTerm();
		while (current.getType() == TokenType.AND) {
			next();
			rhs = bitOrTerm();
			lhs = new BinaryOperation(BinaryOperator.AND, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '|'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression bitOrTerm() {
		Expression lhs, rhs;
		lhs = bitXorTerm();
		while (current.getType() == TokenType.BIT_OR) {
			next();
			rhs = bitXorTerm();
			lhs = new BinaryOperation(BinaryOperator.BITWISE_OR, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '^'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression bitXorTerm() {
		Expression lhs, rhs;
		lhs = bitAndTerm();
		while (current.getType() == TokenType.BIT_XOR) {
			next();
			rhs = bitAndTerm();
			lhs = new BinaryOperation(BinaryOperator.BITWISE_XOR, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '&'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression bitAndTerm() {
		Expression lhs, rhs;
		lhs = eqTerm();
		while (current.getType() == TokenType.BIT_AND) {
			next();
			rhs = eqTerm();
			lhs = new BinaryOperation(BinaryOperator.BITWISE_AND, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '=='/'!='-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression eqTerm() {
		Expression lhs, rhs;
		lhs = relTerm();
		Loop: while (true) {
			BinaryOperator op;
			switch (current.getType()) {
			case EQ:
				op = BinaryOperator.EQUAL;
				break;
			case NE:
				op = BinaryOperator.NOTEQUAL;
				break;
			default:
				break Loop;
			}
			next();
			rhs = relTerm();
			lhs = new BinaryOperation(op, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '>'/'>='/'<'/'<='-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression relTerm() {
		Expression lhs, rhs;
		lhs = addTerm();
		Loop: while (true) {
			BinaryOperator op;
			switch (current.getType()) {
			case LT:
				op = BinaryOperator.LESS_THAN;
				break;
			case GT:
				op = BinaryOperator.GREATER_THAN;
				break;
			case LE:
				op = BinaryOperator.LESS_EQUAL;
				break;
			case GE:
				op = BinaryOperator.GREATER_EQUAL;
				break;
			default:
				break Loop;
			}
			next();
			rhs = addTerm();
			lhs = new BinaryOperation(op, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '+'/'-'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression addTerm() {
		Expression lhs, rhs;
		lhs = mulTerm();
		Loop: while (true) {
			BinaryOperator op;
			switch (current.getType()) {
			case PLUS:
				op = BinaryOperator.ADD;
				break;
			case MINUS:
				op = BinaryOperator.SUBSTRACT;
				break;
			default:
				break Loop;
			}
			next();
			rhs = mulTerm();
			lhs = new BinaryOperation(op, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '*'/'/'/'%'Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression mulTerm() {
		Expression lhs, rhs;
		lhs = unaryPrefixTerm();
		Loop: while (true) {
			BinaryOperator op;
			switch (current.getType()) {
			case TIMES:
				op = BinaryOperator.TIMES;
				break;
			case DIVIDES:
				op = BinaryOperator.DIVIDES;
				break;
			case MOD:
				op = BinaryOperator.MODULOS;
				break;
			default:
				break Loop;
			}
			next();
			rhs = unaryPrefixTerm();
			lhs = new BinaryOperation(op, lhs, rhs);
		}
		return lhs;
	}

	/**
	 * parst einen '!_'/'++_'/'--_'/'-_'/'+_'-Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression unaryPrefixTerm() {
		UnaryOperator op = null;
		switch (current.getType()) {
		case NOT:
			op = UnaryOperator.NOT;
			break;
		case INCR:
			op = UnaryOperator.PREINC;
			break;
		case DECR:
			op = UnaryOperator.PREDEC;
			break;
		case MINUS:
			op = UnaryOperator.MINUS;
			break;
		case PLUS:
			op = UnaryOperator.PLUS;
			break;
		// case NEW: op = UnaryOperator.MOD; break;
		// TODO: spezialfall NEW behandeln
		}
		if (op != null)
			next();
		Expression expr = unaryPostfixTerm();
		if (op != null)
			expr = new UnaryOperation(op, expr);
		return expr;
	}

	/**
	 * parst einen '_++'/'_--'Ausdruck ab dem aktuellen Token
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression unaryPostfixTerm() {
		UnaryOperator op = null;
		Expression expr = factor();
		switch (current.getType()) {
		case INCR:
			op = UnaryOperator.POSTINC;
			break;
		case DECR:
			op = UnaryOperator.POSTDEC;
			break;
		}
		if (op != null) {
			next();
			expr = new UnaryOperation(op, expr);
		}
		return expr;
	}

	/**
	 * parst einen Faaktor-Audruck; das sind alle Ausdrücke der Form:
	 * [id|literal|super|this|id[expr]|id(expr)]
	 * (.[id|literal|id[expr]|id(expr)])*
	 * 
	 * @return den entsprechenden Ausdruck
	 */
	private Expression factor() {
		Expression result = null;
		// Kopfelement
		switch (current.getType()) {
		case STRING: // Literals
		case NULL:
		case FLOAT:
		case INT:
			result = literal();
			break;
		case ID: // Bezeichner, Arrays und Funktionen
			CharSequence name = null;
			try {
				name = current.getString();
			} catch (IllegalAccessException e) {
			}
			next();

			switch (current.getType()) {
			case PAREN_OPEN:
				List<Expression> arguments = actualArguments();
				result = new FunctionCall(name, arguments);
				break;
			case BRACKET_OPEN:
				List<Expression> indicies = arrayAccess();
				result = new ArrayAccess(name, indicies);
				break;
			default:
				result = new Identifier(name);
			}
			break;
		case PAREN_OPEN: // Unterausdruck
			next();
			result = expression();
			if (current.getType() != TokenType.PARENT_CLOSE)
				ErrorHandler.error(null, "missing ')'");
			else
				next();
			break;
		}

		// rest elemente...
		if (current.getType() == TokenType.DOT) {
			List<Expression> chain = new ArrayList<Expression>();
			chain.add(result);
			do {
				if (next().getType() != TokenType.ID) {
					ErrorHandler.error(null, "identifier expected but "
							+ current.getType() + " found.");
					break;
				}

				CharSequence name = null;
				try {
					name = current.getString();
				} catch (IllegalAccessException e) {
				}
				next();

				Expression rhs;
				switch (current.getType()) {
				case PAREN_OPEN:
					List<Expression> arguments = actualArguments();
					rhs = new FunctionCall(name, arguments);
					break;
				case BRACKET_OPEN:
					List<Expression> indicies = arrayAccess();
					rhs = new ArrayAccess(name, indicies);
					break;
				default:
					rhs = new Identifier(name);
				}
				chain.add(rhs);
			} while (current.getType() == TokenType.DOT);

			// baue Syntaxbaum auf
			int size = chain.size();
			result = chain.get(size - 1);
			for (int index = size - 2; index >= 0; --index)
				result = new MemberAccess(chain.get(index), result);
		}
		return result;
	}

	/**
	 * -parst ein Literal (Strings,Zahlen,Null) ab dem aktuellen Token -man
	 * sollte vorher sicherstellen, dass der aktuelle TokenType STRING,ID,NULL
	 * oder FLOAT ist, sonst wird ein Fehler ausgegeben
	 * 
	 * @return gibt dafür den Unterausdruck zurück
	 */
	private Expression literal() {
		Expression result = null;
		switch (current.getType()) {
		case STRING: // Strings
			try {
				result = new StringLiteral(current.getString());
			} catch (IllegalAccessException e) {
			}
			break;
		case NULL: // Null
			result = NullLiteral.NULL;
			break;
		case FLOAT: // Fließkomma
			try {
				result = new FloatLiteral(current.getNumber());
			} catch (IllegalAccessException e) {
			}
			break;
		case INT: // Ganzzahl
			try {
				result = new IntegerLiteral(current.getNumber());
			} catch (IllegalAccessException e) {
			}
			break;
		}
		if (result != null)
			next();
		else
			ErrorHandler.error(null, "no valid literal!");
		return result;
	}

	/**
	 * parst Arrayindizies der Form [Ausdruck][Ausdruck]...
	 * 
	 * @return liefert eine Liste von Ausdrücken zurück
	 */
	private List<Expression> arrayAccess() {
		// linke Klammer!
		assert (current.getType() == TokenType.BRACKET_OPEN);
		next();

		LinkedList<Expression> expressions = new LinkedList<Expression>();

		// Indizies einlesen...
		Loop: for (;;) {
			expressions.add(expression());
			// Klammer muss wieder geschlossen werden
			if (current.getType() != TokenType.BRACKET_CLOSE) {
				ErrorHandler.error(null, "']' exprected, but "
						+ current.getType() + " found.");
				break Loop;
			} else
				next();
			switch (current.getType()) {
			case BRACKET_OPEN: // ein weiterer Index
				next();
				break;
			default: // keine weiteren Indizies mehr
				break Loop;
			}

		}
		return expressions;
	}

	/**
	 * parst eine Funktionsparameterliste der Form (Ausdruck, Ausdruck,
	 * Ausdruck,...)
	 * 
	 * @return liefert eine Liste von Ausdrücken zurück
	 */
	private List<Expression> actualArguments() {
		// linke Klammer!
		assert (current.getType() == TokenType.PAREN_OPEN);
		next();

		LinkedList<Expression> expressions = new LinkedList<Expression>();

		// schon rechte Klammer?
		if (current.getType() == TokenType.PARENT_CLOSE) {
			next();
			return expressions;
		}

		// Argumente einlesen...
		Loop: for (;;) {
			expressions.add(expression());
			switch (current.getType()) {
			case COMMA:
				next();
				break;
			case PARENT_CLOSE:
				next();
				break Loop;
			default:
				ErrorHandler.error(null, "',' or ')' exprected, but "
						+ current.getType() + " found.");
				break Loop;
			}

		}
		return expressions;
	}
}
