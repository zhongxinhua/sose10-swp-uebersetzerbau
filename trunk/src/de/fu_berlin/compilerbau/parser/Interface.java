package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br> {@link Interface} is a subclass of
 * {@link ClassOrInterface} representing a &ltinterface/&gt statement.<br>
 * <p>
 * <b>Specification</b><br>
 * The &ltinterface/&gt statement <b>needs one</b> attribute:
 * <ul>
 * <li>name - the name of the interface</li>
 * </ul>
 * The &ltinterface/&gt statement <b>has no</b> optional attributes
 * <p>
 * The &ltinterface/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> &ltfunction/&gt statements with empty function bodys</li>
 * </ul>
 * 
 * @author Sam
 * 
 */
public class Interface extends ClassOrInterface {
	private List<Function> functions;
	private List<ExtendsStatement> extensions = new LinkedList<ExtendsStatement>();

	public List<Function> getFunctions() {
		return functions;
	}
	
	public List<ExtendsStatement> getExtensions() {
		return extensions;
	}

	public Interface(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'name' attribute expected");
		}
		// process child nodes
		for (DomNode child : node.getChilds()) {
			if (child.getName().equals("function")) {
				Function tmp = new Function(child);
				if (tmp.hasBody()) {
					ErrorHandler
							.error(node,
									"'interface' has to have empty function declarations!");
				} else {
					functions.add(tmp);
				}

			} else if (child.getName().equals("extends")) {
				extensions.add(new ExtendsStatement(child));
			} else {
				// ERROR -> forbidden statement
				ErrorHandler.error(child, this.getClass().toString()
						+ " unknown statement: " + child.getName());
			}
		}

	}

}