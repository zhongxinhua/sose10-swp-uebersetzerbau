package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.LeftHandSide;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link SetStatement} is a subclass of {@link Statement}
 * representing a &ltset/&gt statement forming a node in the parse tree.
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltset/&gt statement <b>needs two</b> attributes:
 * <ul>
 * <li>name - the name of the variable to set, must be declared before</li>
 * <li>value - the assigned value of the variable, must be a {@link Expression}</li>
 * </ul>
 * The &ltset/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltset/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * 
 */

public class SetStatement extends Statement {
	LeftHandSide lvalue;
	Expression rvalue;

	public SetStatement(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			System.out.println("DEBUG: UNIMPLEMENTED: SetStatement");
			System.err.println("DEBUG: UNIMPLEMENTED: SetStatement");
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'name' attribute expected");
		}
		// check needed attribute: value
		if (node.hasAttribute("value")
				&& node.getAttributeValue("value").length() > 0) {
			rvalue = Expression.build(node.getAttribute("value"));
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

}