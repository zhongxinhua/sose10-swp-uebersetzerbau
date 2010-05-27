package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link ImplementStatement} is a subclass of
 * {@link Statement} representing a &ltimplement/&gt statement used in
 * &ltclass/&gt statements to define the interface a class is implementing.
 * <p/>
 * <b>Specification</b><br>
 * The &ltimplement/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the interface to implement</li>
 * </ul>
 * The &ltimplement/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltimplement/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * @see {@link Class}
 */
public class ImplementStatement extends Statement {
	PositionString name;

	public ImplementStatement(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, "'name' attribute expected");
		}
		// check for body forbidden restriction
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " body forbidden!");
		}

	}

}
