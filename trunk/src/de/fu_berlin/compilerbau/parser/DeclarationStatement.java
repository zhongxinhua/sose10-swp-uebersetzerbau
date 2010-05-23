package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * {@link DeclarationStatement} is a subclass of {@link Statement} representing
 * a &ltdecl/&gt statement forming a node in the parse tree. It is common used
 * as a variable with an explicit type, but may also be declared as an array
 * (with attribute dim="x", where x > 1) or as an objectreference.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltdecl/&gt statement needs following attributes:
 * <ul>
 * <li>type - the type of the variable to declare, must be a subclass of
 * {@link Type}</li>
 * <li>name - the name of the variable to declare, must be unique in the scope</li>
 * </ul>
 * The &ltdecl/&gt statement has following optional attributes:
 * <ul>
 * <li>dim - an {@link int} &gt= 0 representing the dimension, default=1</li>
 * <li>value - the assigned value of the variable, must be {@link Expression},
 * default={@link null}</li>
 * <li>static - the {@link String} "yes" turns declaration static, default="no"</li>
 * <li>final - the {@link String} "yes" turns declaration final, default="no"</li>
 * </ul>
 * <p>
 * The &ltdecl/&gt body is <b>forbidden</b> to exist.
 * 
 * @author Sam
 * 
 */

public class DeclarationStatement extends Statement {

	Type type;
	String name;
	int dimension = 1;
	Expression value = null;
	boolean isStatic = false;
	boolean isFinal = false;

	public DeclarationStatement(DomNode node) {
		// check needed attribute: type
		if (node.hasAttribute("type")
				&& node.getAttributeValue("type").length() > 0) {
			this.type = Type.get(node.getAttributeValue("type"));
			// nicht besonders schön, aber funzt
			if (type == null) {
				ErrorHandler.error(node,
						"'type' attribute parse error: unknown type: "
								+ node.getAttributeValue("type"));
			}
		} else {
			ErrorHandler.error(node, "'type' attribute expected");
		}

		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			this.name = node.getAttributeValue("name");
		} else {
			ErrorHandler.error(node, "'name' attribute expected");
		}

		// check optional attribute: dim
		if (node.hasAttribute("dim")
				&& !node.getAttributeValue("dim").equals("")) {
			try {
				this.dimension = Integer.parseInt(node.getAttributeValue("dim")
						.toString());
			} catch (Exception e) {
				ErrorHandler.error(node, "'dim' attribute parse error: "
						+ e.getMessage());
			}

		}

		// check optional attribute: value
		if (node.hasAttribute("value")
				&& !node.getAttributeValue("value").equals("")) {
			this.value = Expression.build(node.getAttribute("value"));
		}

		// check optional attribute: static
		if (node.hasAttribute("static")
				&& !(node.getAttributeValue("static").compareTo("")==0)) {
			if (node.getAttributeValue("static").compareTo("yes")==0) {
				this.isStatic = true;
			} else {
				ErrorHandler.error(node,
						"'static' attribute parse error: 'yes' expected");
			}
		}

		// check optional attribute: final
		if (node.hasAttribute("final")
				&& !(node.getAttributeValue("final").compareTo("")==0)) {
			if (node.getAttributeValue("final").compareTo("yes")==0) {
				isFinal = true;
			} else {
				ErrorHandler.error(node,
						"'final' attribute parse error: 'yes' expected");
			}
		}

	}

}