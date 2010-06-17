package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.Type;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br> {@link DeclarationStatement} is a subclass of
 * {@link Statement} representing a &ltdecl/&gt statement. It is common used as
 * a variable with an explicit type, but may also be declared as an array (with
 * attribute dim="x", where x > 1) or as an objectreference.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltdecl/&gt statement <b>needs two</b> attributes:
 * <ul>
 * <li>type - the type of the variable to declare, must be a subclass of
 * {@link Type}</li>
 * <li>name - the name of the variable to declare, must be unique in the scope</li>
 * </ul>
 * The &ltdecl/&gt statement <b>has four</b> optional attributes:
 * <ul>
 * <li>dim - an {@link int} &gt= 0 representing the dimension (default=1)</li>
 * <li>value - the assigned value of the variable, must be {@link Expression}
 * (default={@link null})</li>
 * <li>static - the {@link String} "yes" turns declaration static (default="no")
 * </li>
 * <li>final - the {@link String} "yes" turns declaration final (default="no")</li>
 * </ul>
 * <p>
 * The &ltdecl/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * @see {@link Expression}
 * @see {@link Type}
 * 
 */

public class DeclarationStatement extends Statement {

	private Type type;
	private PositionString name;
	private int dimension = 0;
	private Expression value = null;
	private boolean isStatic = false;
	private boolean isFinal = false;

	public DeclarationStatement(DomNode node) {
		// check needed attribute: type
		if (node.hasAttribute("type")
				&& node.getAttributeValue("type").length() > 0) {
			this.type = Type.get(node.getAttributeValue("type"));
			// nicht besonders schï¿½n, aber funzt
			if (type == null) {
				ErrorHandler.error(node,
						"'type' attribute parse error: unknown type: "
								+ node.getAttributeValue("type"));
			}
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'type' attribute expected");
		}

		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			//Symboltabellenanbindung-> überprüfe reservierte/vergebene Namen
				//Symboltable.check(node.getAttributeValue("name"));
			//
			this.name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node, this.getClass().toString()
					+ " 'name' attribute expected");
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
			this.value = Expression.build(node.getAttribute("value"),
					ExpressionType.RVALUE);
		}

		// check optional attribute: static
		if (node.hasAttribute("static")
				&& (node.getAttributeValue("static").length() != 0)) {
			if (node.getAttributeValue("static").equals("yes")) {
				this.isStatic = true;
			} else {
				ErrorHandler.error(node,
						"'static' attribute parse error: 'yes' expected");
			}
		}

		// check optional attribute: final
		if (node.hasAttribute("final")
				&& (node.getAttributeValue("final").length() != 0)) {
			if (node.getAttributeValue("final").equals("yes")) {
				isFinal = true;
			} else {
				ErrorHandler.error(node,
						"'final' attribute parse error: 'yes' expected");
			}
		}

		// check if statement is a Leaf
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()
					+ " has to be a Leaf!");
		}
	}

	// BEGIN get-Methoden fï¿½r Builder
	public Type getType() {
		return type;
	}

	public PositionString getName() {
		return name;
	}

	public int getDimension() {
		return dimension;
	}

	public Expression getValue() {
		return value;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public boolean isArray() {
		return dimension > 0;
	}
	// END get-Methoden fï¿½r Builder
}