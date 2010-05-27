package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link BreakStatement} is a subclass of {@link Statement}
 * representing a &ltbreak/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltbreak/&gt statement <b>has no</b> attributes.
 * <p/>
 * The &ltbreak/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltbreak/&gt statement <b>must be</b> a Leaf.
 * 
 * @see {@link DoStatement}
 * @author Sam
 * 
 */

public class BreakStatement extends Statement {

	public BreakStatement(DomNode node) {
		// check for empty attribute list
		if (!node.getAttributes().isEmpty()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " attributes forbidden!");
		}
		// check for body forbidden restriction
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " body forbidden!");
		}
	}

}
