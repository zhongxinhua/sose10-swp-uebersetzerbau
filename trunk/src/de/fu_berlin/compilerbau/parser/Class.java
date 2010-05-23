package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
/**
 * {@link Class} is a subclass of {@link ClassOrInterface} representing a
 * &ltclass/&gt statement forming a node in the parse tree.<br>
 * <p>
 * <b>Specification</b><br>
 * The &ltclass/&gt statement needs following attributes:
 * <ul>
 * <li>name - the name of the class</li>
 * </ul>
 * The &ltclass/&gt statement has following optional attributes:
 * <ul>
 * <li>super - the name of the superclass</li>
 * </ul>
 * <p>
 * The &ltclass/&gt statement body has:
 * <ul>
 * <li>arbitrary &ltimport/&gt statements</li>
 * <li>arbitrary &ltimplements/&gt statements</li>
 * <li>arbitrary &ltdecl/&gt statements</li>
 * <li>arbitrary &ltfunction/&gt statements</li>
 * <li>arbitrary &ltstatic/&gt statements</li>
 * </ul>
 * 
 * @author Sam
 * 
 */
public class Class extends ClassOrInterface {
	Class parent;
	List<ImportStatement> imports = new LinkedList<ImportStatement>();
	List<ImplementStatement> implementations = new LinkedList<ImplementStatement>();
	/*
	 * ausgetauscht durch neue Klasse ImplementStatemnt List<Reference>
	 * interfaces = new LinkedList<Reference>();
	 */
	private List<DeclarationStatement> declarations = new LinkedList<DeclarationStatement>();
	private List<Function> functions = new LinkedList<Function>();
	List<StaticStatement> statics = new LinkedList<StaticStatement>();

	public Class(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, "'name' attribute expected");
		}
		// check optional attribute: super
		if (node.hasAttribute("super")
				&& !node.getAttributeValue("super").equals("")) {
			// TODO parent referenzieren über symboltabelleneintrag

		}

		// process child nodes
		
		for (DomNode child : node.getChilds()) {
			if (child.getName().compareTo("import")==0) {
				imports.add(new ImportStatement(child));
				continue;
			} else if (child.getName().compareTo("implements")==0) {
				implementations.add(new ImplementStatement(child));
				continue;
			} else if (child.getName().compareTo("decl")==0) {
				declarations.add(new DeclarationStatement(child));
				continue;
			} else if (child.getName().compareTo("function")==0) {
				functions.add(new Function(child));
				continue;
			} else if (child.getName().compareTo("static")==0) {
				statics.add(new StaticStatement(child));
				continue;
			} else {
				// ERROR
				ErrorHandler.error(child, "unknown statement");
			}

		}
	}

	public List<DeclarationStatement> getDeclerations() { return declarations; }
	public List<Function> getFunctions() { return functions; }
}