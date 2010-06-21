package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link Module} represents a &ltmodule/&gt statement and forms the root of the
 * parse tree.<br>
 * First a check for the requested attributes is performed. Afterwards each
 * child node is processed.
 * <p>
 * <b>Specification</b><br>
 * The &ltmodule/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the module</li>
 * </ul>
 * The &ltmodule/&gt statement <b>has no</b> optional attributes.
 * <p>
 * The &ltmodule/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> &ltimport/&gt statements</li>
 * <li><b>arbitrary</b> &ltclass/&gt statements</li>
 * <li><b>arbitrary</b> &ltinterface/&gt statements</li>
 * </ul>
 * 
 * 
 * 
 * @author Sam, stefan
 * 
 */
public class Module extends SyntaxTreeNode {
	/**
	 * name of the module
	 */
	private PositionString name;
	/**
	 * list of &ltimport/&gt statements
	 */
	private List<ImportStatement> imports = new LinkedList<ImportStatement>();
	/**
	 * list of &ltclass/&gt statements
	 */
	private List<Class> classes = new LinkedList<Class>();
	/**
	 * list of &ltinterface/&gt statements
	 */
	private List<Interface> interfaces = new LinkedList<Interface>();

	/**
	 * default constructor called by {@link AbstractSyntaxTree}, asserting a
	 * {@link DomNode} representing a &ltmodule/&gt statement.
	 * 
	 * @param node
	 *            {@link DomNode} representating a &ltmodule/&gt statement
	 */
	public Module(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, "'name' attribute expected");
		}
		// process child nodes
		for (DomNode child : node.getChilds()) {
			if (child.getName().equals("import")) {
				imports.add(new ImportStatement(child));
				continue;
			} else if (child.getName().equals("class")) {
				classes.add(new Class(child));
				continue;

			} else if (child.getName().equals("interface")) {
				interfaces.add(new Interface(child));
			} else {
				// ERROR
				ErrorHandler.error(child, "unknown statement: "+child.getName());
			}
		}

	}

	public PositionString getName() {
		return name;
	}
	
	public List<Class> getClasses() {
		return classes;
	}
	
	public List<Interface> getInterfaces() {
		return interfaces;
	}
}
