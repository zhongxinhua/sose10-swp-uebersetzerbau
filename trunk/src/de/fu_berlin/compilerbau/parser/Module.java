package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * {@link Module} represents a &ltmodule/&gt statement and forms the root of the
 * parse tree.<br>
 * First a check for the requested attributes is performed. After verifying the
 * passed {@link DomNode} is 'module' each child node is created using
 * {@link ClassOrInterface} constructor and after return added to the module's
 * declarations list.
 * <p>
 * <b>Specification</b><br>
 * The &ltmodule/&gt statement needs following attributes:
 * <ul>
 * <li>name - the name of the module</li>
 * </ul>
 * The &ltmodule/&gt statement has no optional attributes.
 * <p>
 * The &ltmodule/&gt body may have:
 * <ul>
 * <li>arbitrary &ltimport/&gt statements</li>
 * <li>arbitrary &ltclass/&gt statements</li>
 * <li>arbitrary &ltinterface/&gt statements</li>
 * </ul>
 * 
 * 
 * 
 * @author Sam
 * 
 */
public class Module {

	PositionString name;
	// List<Type> imports;
	List<ClassOrInterface> declarations = new LinkedList<ClassOrInterface>();;

	/**
	 * default constructor called by {@link AbstractSyntaxTree}, expecting a
	 * {@link DomNode} representing a &ltmodule/&gt statement.
	 * 
	 * @param node
	 *            {@link DomNode} representating a &ltmodule/&gt statement
	 */
	public Module(DomNode node) {
		if (node.getName().equals("module")) {
			if (node.hasAttribute("name")
					&& node.getAttributeValue("name").length() > 0) {
				name = node.getAttributeValue("name");

				for (DomNode child : node.getChilds()) {
					ClassOrInterface c = ClassOrInterface.build(child);
					if (c != null)
						declarations.add(c);
				}
			} else
				ErrorHandler.error(node, "'name' attribute expected");
		} else
			ErrorHandler.error(node, "'module' expected");
	}
}
