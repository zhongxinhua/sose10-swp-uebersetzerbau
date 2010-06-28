package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link ExtendsStatement} is a subclass of
 * {@link Statement} representing a &ltimplement/&gt statement used in
 * &ltclass/&gt statements to define the interface a class is implementing.
 * <p/>
 * <b>Specification</b><br>
 * The &ltexteds/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the interface to implement</li>
 * </ul>
 * The &ltextends/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltextends/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * @see {@link Class}
 */
public class ExtendsStatement extends Statement {
	private PositionString name;

	public ExtendsStatement(DomNode node) {
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
	// BEGIN get-Methoden für Builder
	public PositionString getName(){
		return name;
	}
	// END get-Methoden für Builder
	
	@Override
	public String toString() {
		return "extends "+name;
	}
}
