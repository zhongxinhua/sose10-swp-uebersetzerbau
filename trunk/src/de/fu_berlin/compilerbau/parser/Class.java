package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**<b>Description</b><br>
 * {@link Class} is a subclass of {@link ClassOrInterface} representing a
 * &ltclass/&gt statement forming a node in the parse tree.<br>
 * <p>
 * <b>Specification</b><br>
 * The &ltclass/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the class</li>
 * </ul>
 * The &ltclass/&gt statement <b>has one</b> optional attribute:
 * <ul>
 * <li>super - the name of the superclass</li>
 * </ul>
 * <p>
 * The &ltclass/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> &ltimport/&gt statements</li>
 * <li><b>arbitrary</b> &ltimplements/&gt statements</li>
 * <li><b>arbitrary</b> &ltdecl/&gt statements</li>
 * <li><b>arbitrary</b> &ltfunction/&gt statements</li>
 * <li><b>arbitrary</b> &ltstatic/&gt statements</li>
 * </ul>
 * 
 * @author Sam
 * 
 */
public class Class extends ClassOrInterface {
	PositionString parent;
	List<ImportStatement> imports = new LinkedList<ImportStatement>();
	List<ImplementStatement> implementations = new LinkedList<ImplementStatement>();
	List<DeclarationStatement> declarations = new LinkedList<DeclarationStatement>();
	List<Function> functions = new LinkedList<Function>();

	public Class(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'name' attribute expected");
		}
		// check optional attribute: super
		if (node.hasAttribute("super")
				&& !node.getAttributeValue("super").equals("")) {
			// TODO parent referenzieren �ber symboltabelleneintrag
			parent = node.getAttribute("super");
		}

		// process child nodes
		for (DomNode child : node.getChilds()) {
			// check arbitrary import statements
			if (child.getName().compareTo("import") == 0) {
				imports.add(new ImportStatement(child));
				continue;
				// check arbitrary implement statements
			} else if (child.getName().compareTo("implements") == 0) {
				implementations.add(new ImplementStatement(child));
				continue;
				// check arbitrary decl statements
			} else if (child.getName().compareTo("decl") == 0) {
				declarations.add(new DeclarationStatement(child));
				continue;
				// check arbitrary function statements
			} else if (child.getName().compareTo("function") == 0) {
				functions.add(new Function(child));
				continue;
			} else {
				// ERROR
				ErrorHandler.error(child, this.getClass().toString()
						+ " unknown statement: " + child.getName());
			}

		}
	}

	public List<DeclarationStatement> getDeclerations() {
		return declarations;
	}

	public List<Function> getFunctions() {
		return functions;
	}
}