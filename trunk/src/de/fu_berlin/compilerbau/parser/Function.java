package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Type;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * <b>Description</b><br>{@link Function} is representing a &ltfunction/&gt
 * statement.
 * <p/>
 * <b>Specification</b><br>
 * The &ltfunction/&gt statement <b>needs one</b> attributes:
 * <ul>
 * <li>name - the name of the function</li>
 * 
 * </ul>
 * The &ltfunction/&gt statement <b>has three</b> optional attributes:
 * <ul>
 * <li>returns - declares the {@link Type} of the return value
 * <li>static - determines whether the function is static. The {@link String}
 * "yes" sets the function static (default="no")</li>
 * <li>final - determines whether the function ist final. The {@link String}
 * "yes" sets the function final (default="no")</li>
 * </ul>
 * <p/>
 * The &ltfunction/&gt statement body has:
 * <ul>
 * <li><b>exact one</b> &ltarguments/&gt statement</li>
 * <li><b>arbitrary</b> statements</li>
 * <li><b>at least one</b> &ltreturn/&gt statement
 * </ul>
 * 
 * 
 * @author Sam
 * @see {@link ArgumentStatement}
 * @see {@link Statement}
 * @see {@link ReturnStatement}
 * 
 */
public class Function extends SyntaxTreeNode {
	private PositionString name;
	private Type return_type;
	// default: NO
	boolean isStatic = false;
	// default: NO
	boolean isFinal = false;
	private List<Statement> body = new LinkedList<Statement>();
	private List<DeclarationStatement> arguments = new LinkedList<DeclarationStatement>();

	public Function(DomNode node) {
		// check needed attribute: name
		if (node.hasAttribute("name")
				&& node.getAttributeValue("name").length() > 0) {
			name = node.getAttribute("name");
		} else {
			ErrorHandler.error(node,
					"'name' attribute expected at function head");
		}
		// check optional attribute: returns
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
				&& !(node.getAttributeValue("static").compareTo("") == 0)) {
			if (node.getAttributeValue("static").compareTo("yes") == 0) {
				this.isStatic = true;
			} else {
				ErrorHandler.error(node,
						"'static' attribute parse error: 'yes' expected");
			}
		}
		// check optional attribute: final
		if (node.hasAttribute("final")
				&& !(node.getAttributeValue("final").compareTo("") == 0)) {
			if (node.getAttributeValue("final").compareTo("yes") == 0) {
				isFinal = true;
			} else {
				ErrorHandler.error(node,
						"'final' attribute parse error: 'yes' expected");
			}
		}

		// process child nodes
		for (DomNode child : node.getChilds()) {
			if (child.getName().compareTo("arguments") == 0) {
				// check for empty attribute list
				if (!child.getAttributes().isEmpty()) {
					ErrorHandler.error(child, this.getClass().toString()
							+ " attributes forbidden!");
				}
				// process childs
				for (DomNode arg : child.getChilds()) {
					// arbitrary decl statements
					if (arg.getName().compareTo("decl") == 0) {
						arguments.add(new DeclarationStatement(arg));
					} else {
						// ERROR
						ErrorHandler.error(child, this.getClass().toString()
								+ " forbidden use: " + node.getName());
					}
				}
			} else if (child.getName().compareTo("return") == 0) {
				System.out.println("DEBUG2::Function::ReturnStatement");
				body.add(new ReturnStatement(child));
			} else {
				Statement stmt = new Statement(child);
				System.out.println("DEBUG2::Function::"+stmt.statement);
				body.add(stmt.statement);
			}
		}
	}

	public boolean hasBody() {
		return !body.isEmpty();
	}

	public Type getReturnType() {
		return return_type;
	}

	public PositionString getName() {
		return name;
	}
	public boolean isFinal() {
		return isFinal;
	}
	public boolean isStatic() {
		return isStatic;
	}
	public List<DeclarationStatement> getArguments() {
		return arguments;
	}

	public List<Statement> getBody() {
		return body;
	}
}