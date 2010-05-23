package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * {@link Function} is representing a &ltfunction/&gt statement forming a node
 * in the parse tree.<br>
 * <p/>
 * <b>Specification</b><br>
 * The &ltfunction/&gt statement needs following attributes:
 * <ul>
 * <li>name - the name of the function</li>
 * <li>returns - declares the {@link Type} of the return value
 * </ul>
 * The &ltfunction/&gt statement has following optional attributes:
 * <ul>
 * <li>static - the {@link String} "yes" turns function static, default="no"</li>
 * <li>final - the {@link String} "yes" turns function final, default="no"</li>
 * </ul>
 * <p/>
 * The &ltfunction/&gt statement body has:
 * <ul>
 * <li>exact one &ltarguments/&gt statement</li>
 * <li>arbitrary &ltdecl/&gt statements</li>
 * <li>at least one &ltreturn/&gt statement
 * </ul>
 * 
 * 
 * @author Sam
 * @see {@link ArgumentStatement}
 * @see {@link DeclarationStatement}
 * @see {@link ReturnStatement}
 * 
 */
public class Function {
	PositionString name;
	Type return_type;
	// default: NO
	boolean isStatic = false;
	// default: NO
	boolean isFinal = false;
	ArgumentStatement arguments;
	List<Statement> body;
	ReturnStatement returns;

	public Function(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttributeValue("name");
		} else {
			ErrorHandler.error(node,
					"'name' attribute expected at function head");
		}
		// check needed attribute: returns
		if (node.hasAttribute("returns")
				&& node.getAttributeValue("returns").length() > 0) {
			return_type = Type.get(node.getAttributeValue("returns"));
			if (return_type == null) {
				ErrorHandler.error(node,
						"'type' attribute parse error: unknown type: "
								+ node.getAttributeValue("type"));
			}
		}
		// check optional attribute: static
		if (node.hasAttribute("static")
				&& !node.getAttributeValue("static").equals("")) {
			if (node.getAttributeValue("static").equals("yes")) {
				this.isStatic = true;
			} else {
				ErrorHandler.error(node,
						"'static' attribute parse error: 'yes' expected");
			}
		}
		// check optional attribute: final
		if (node.hasAttribute("final")
				&& !node.getAttributeValue("final").equals("")) {
			if (node.getAttributeValue("final").equals("yes")) {
				isFinal = true;
			} else {
				ErrorHandler.error(node,
						"'final' attribute parse error: 'yes' expected");
			}
		}

		// process child nodes
		for (DomNode child : node.getChilds()) {
			if (child.getName().equals("arguments")) {
				arguments = new ArgumentStatement(child);
				continue;
			} else if (child.getName().equals("decl")) {
				body.add(new DeclarationStatement(child));
				continue;

			} else if (child.getName().equals("returns")) {
				returns = new ReturnStatement(child);
				continue;

			} else {
				// ERROR
				ErrorHandler.error(child, "unknown statement");
			}

		}
		
		if(returns==null){
			ErrorHandler.error(node, "returns statement missing");
		}

	}

	public boolean hasBody() {

		return !body.isEmpty();

	}
}