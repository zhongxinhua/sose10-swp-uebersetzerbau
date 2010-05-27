package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br> {@link ArgumentStatement} is a subclass of
 * {@link Statement} representing a &ltarguments/&gt statement used in a
 * &ltfunction/&gt statement.
 * <p>
 * <b>Specification</b><br>
 * The &ltarguments/&gt statement <b>has no</b> attributes.
 * <p/>
 * The &ltarguments/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltarguments/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> &ltdecl/&gt statements describing the functions
 * parameters.</li>
 * </ul>
 * 
 * @see {@link DeclarationStatement}
 * @see {@link Function}
 * @author Sam
 * 
 */
public class ArgumentStatement extends Statement {
	List<DeclarationStatement> arguments = new LinkedList<DeclarationStatement>();

	public ArgumentStatement(DomNode node) {
		// check for empty attribute list
		if (!node.getAttributes().isEmpty()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " attributes forbidden!");
		}
		// process childs
		for (DomNode child : node.getChilds()) {
			// arbitrary decl statements
			if (child.getName().compareTo("decl") == 0) {
				arguments.add(new DeclarationStatement(child));
			} else {
				// ERROR
				ErrorHandler.error(child, this.getClass().toString()
						+ " forbidden use: " + node.getName());
			}

		}
	}

}
