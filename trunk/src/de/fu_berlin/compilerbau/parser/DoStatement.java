package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link DoStatement} is a subclass of {@link Statement}
 * representing a &ltdo/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltdo/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>test - the boolean test to perform</li>
 * </ul>
 * <p/>
 * The &ltdo/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltdo/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> statements</li>
 * </ul>
 * 
 * @see {@link Case}
 * @see {@link Statement}
 * @author Sam
 * 
 */
public class DoStatement extends Statement {
	Expression test;
	List<Statement> body = new LinkedList<Statement>();

	public DoStatement(DomNode node) {
		// check needed attribute: test
		if (node.hasAttribute("test")
				&& node.getAttributeValue("test").length() > 0) {
			test = Expression.build(node.getAttribute("test"), ExpressionType.RVALUE);
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'test' attribute expected");
		}

		// process child nodes, if exist
		if (node.isNode()) {
			for (DomNode child : node.getChilds()) {
				Statement stmt = Statement.build(child);
				body.add(stmt);
			}
		}
	}
	// BEGIN get-Methoden f�r Builder
	public Expression getTest(){
		return test;
	}
	public List<Statement> getBody(){
		return body;
	}
	// END get-Methoden f�r Builder

}