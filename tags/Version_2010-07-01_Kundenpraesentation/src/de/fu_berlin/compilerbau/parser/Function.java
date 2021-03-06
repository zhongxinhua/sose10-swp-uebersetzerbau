package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
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
 * The &ltfunction/&gt statement <b>has four</b> optional attributes:
 * <ul>
 * <li>returns - declares the type of the return value
 * <li>dim - declares the dimension of the return type
 * <li>static - determines whether the function is static. The {@link String}
 * <li>returns - declares the {@link Type} of the return value</li>
 * <li>dim - declares the dimension of the return value (default=0)</li>
 * <li>static - determines whether the function is static. The {@link String}</li>
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
@SuppressWarnings("serial")
public class Function extends SyntaxTreeNode {
	private PositionString name;
	private int returnTypeDimension;
	private PositionString returnType;
	// default: NO
	private boolean isStatic = false;
	// default: NO
	private boolean isFinal = false;
	private List<Statement> body = new LinkedList<Statement>();
	private List<DeclarationStatement> arguments = new LinkedList<DeclarationStatement>();

	public Function(DomNode node) {
		setPosition(node);
		
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
			returnType = node.getAttribute("returns");
			if (returnType == null) {
				ErrorHandler.error(node,
						"'type' attribute parse error: unknown type: "
								+ node.getAttributeValue("type"));
			}
		}
		// check optional attribute: dim
		returnTypeDimension = 0;
		if (node.hasAttribute("dim")
				&& node.getAttributeValue("dim").length() > 0) {
			try {
				returnTypeDimension = Integer.parseInt(node.getAttributeValue("dim"));
			} catch(NumberFormatException e) {
				ErrorHandler.error(node.getAttribute("dim"), "dim-attribute expects an integer value");
			}
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

		// process child nodes
		for (DomNode child : node.getChilds()) {
			if (child.getName().equals("arguments")) {
				// check for empty attribute list
				if (!child.getAttributes().isEmpty()) {
					ErrorHandler.error(child, this.getClass().toString()
							+ " attributes forbidden!");
				}
				// process childs
				for (DomNode arg : child.getChilds()) {
					// arbitrary decl statements
					if (arg.getName().equals("decl")) {
						arguments.add(new DeclarationStatement(arg));
					} else {
						// ERROR
						ErrorHandler.error(child, this.getClass().toString()
								+ " forbidden use: " + node.getName());
					}
				}
			} else if (child.getName().equals("return")) {
				body.add(new ReturnStatement(child));
			} else {
				Statement stmt = Statement.build(child);
				body.add(stmt);
			}
		}
	}

	public boolean hasBody() { return !body.isEmpty(); }
	public PositionString getReturnType() { return returnType; }
	public int getReturnTypeDimension() { return returnTypeDimension; }
	public boolean returnsArray() { return returnTypeDimension>0; }
	public PositionString getName() { return name; }
	public boolean isStatic() { return isStatic; }
	public boolean isFinal() { return isFinal; }
	public List<Statement> getBody() { return body; }
	public List<DeclarationStatement> getArguments() { return arguments; }
	
	@Override
	public String toString() {
		String strParams = "";
		if(arguments.size()>0) {
			DeclarationStatement lastArg = arguments.get(arguments.size()-1);
			for(DeclarationStatement arg  : arguments)
				strParams += arg.getName() + (lastArg != arg ? "," : "");
		}
		return "function "+name+"("+strParams+")";
	}
}