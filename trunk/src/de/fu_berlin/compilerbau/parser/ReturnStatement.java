package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link ReturnStatement} is a subclass of {@link Statement} representing a
 * &ltreturns/&gt statement being a required part of a &ltfunction/&gt
 * statement.
 * <p/>
 * <b>Specification</b><br>
 * The &ltreturns/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>value - the value a function returns</li>
 * </ul>
 * The &ltreturns/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltreturns/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * 
 */
public class ReturnStatement extends Statement {
	Expression value;

	public ReturnStatement(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("value")
				&& node.getAttributeValue("value").length() > 0) {
			this.value = Expression.build(node.getAttribute("value"), ExpressionType.RVALUE);
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'value' attribute expected");
		}

		// check for body forbidden restriction
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " body forbidden!");
		}
	}
	//BEGIN get-Methoden f�r Builder
	public Expression getValue(){
		return value;
	}
	//END get-Methoden f�r Builder
	
	@Override
	public String toString() {
		return "return "+value.toString()+";";
	}
}