package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link ImportStatement} is a subclass of {@link Statement} representing a
 * &ltimport/&gt statement.
 * <p>
 * <b>Specification</b><br>
 * The &ltimport/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the class to import</li>
 * </ul>
 * The &ltimport/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltimplement/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * 
 */
public class ImportStatement extends SyntaxTreeNode {

	PositionString name;

	public ImportStatement(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			this.name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'name' attribute expected");
		}
		
		
		
		//check for body forbidden restriction
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()+" body forbidden!");
		}
	}
	// BEGIN get-Methoden f�r Builder
	public PositionString getName(){
		return name;
	}
	// END get-Methoden f�r Builder

	@Override
	public String toString() {
		return "import "+name;
	}
}
