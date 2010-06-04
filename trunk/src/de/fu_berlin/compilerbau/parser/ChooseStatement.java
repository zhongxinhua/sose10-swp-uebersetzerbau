package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**<b>Description</b><br>
 * {@link ChooseStatement} is a subclass of {@link Statement} representing a
 * &ltchoose/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltchoose/&gt statement <b>has no</b> attributes.
 * <p/>
 * The &ltchoose/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltchoose/&gt statement body has:
 * <ul>
 * <li><b>at least one</b> &ltcase/&gt</li> statement
 * </ul>
 * 
 * @see {@link Case}
 * @author Sam
 * 
 */

public class ChooseStatement extends Statement {
	List<Case> cases = new LinkedList<Case>();

	public ChooseStatement(DomNode node) {
		// check for empty attribute list
		if (!node.getAttributes().isEmpty()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " attributes forbidden!");
		}
		// process child nodes
		// check for at least one child
		if (!node.isLeaf()) {
			for (DomNode child : node.getChilds()) {
				if (child.getName().compareTo("case") == 0) {
					cases.add(new Case(child));
				}
			}
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " at least one child needed!");
		}
	}
	// BEGIN get-Methoden f�r Builder
	public List<Case> getCases(){
		return cases;
	}
	// END get-Methoden f�r Builder

}