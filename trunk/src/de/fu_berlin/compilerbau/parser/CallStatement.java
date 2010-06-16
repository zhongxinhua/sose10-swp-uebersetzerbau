package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link CallStatement} is a subclass of {@link Statement}
 * representing a &ltcall/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltcall/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>exec - the code which is to execute</li>
 * </ul>
 * <p/>
 * The &ltcall/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltcall/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * 
 */

public class CallStatement extends Statement {

	private Expression call;
	private PositionString value = null;

	public CallStatement(DomNode node) {
		// check needed attribute: exec
		if (node.hasAttribute("exec")
				&& node.getAttributeValue("exec").length() > 0) {
			call = Expression.build(node.getAttribute("exec"), ExpressionType.FUNCTIONCALL);
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'exec' attribute expected");
		}

		if (node.hasAttribute("value")
			&& node.getAttributeValue("value").length() > 0) {
			this.value = node.getAttribute("value");
		}
		
		// check if statement is a Leaf
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " has to be a Leaf!");
		}
	}
	
	public Expression getCall() { return call; }
	public PositionString getValue() { return value; }
}
