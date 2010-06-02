package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link Statement} is a superclass of all kind of statements that can appear
 * in a function body. 
 * <p/>
 * <b>Specification</b><br>
 * There are following subclasses of statement:
 * <ul>
 * 
 * <li>&ltbreak/&gt statement, see also: {@link BreakStatement}</li>
 * <li>&ltcall/&gt statement, see also: {@link CallStatement}</li>
 * <li>&ltchoose/&gt statement, see also: {@link ChooseStatement}</li>
 * <li>&ltcontinue/&gt statement, see also: {@link ContinueStatement}</li>
 * <li>&ltdecl/&gt statement, see also: {@link DeclarationStatement}</li>
 * <li>&ltdo/&gt statement, see also: {@link DoStatement}</li>
 * <li>&ltreturn/&gt statement, see also: {@link ReturnStatement}</li>
 * <li>&ltscope/&gt statement, see also: {@link ScopeStatement}</li>
 * <li>&ltset/&gt statement, see also: {@link SetStatement}</li>
 * </ul>
 * 
 * 
 * @author Sam
 * 
 */
public class Statement extends SyntaxTreeNode {
	Statement statement;

	public Statement() {

	}

	public Statement(DomNode node) {
		
		if (node.getName().compareTo("break") == 0) {
			statement = new BreakStatement(node);
		}
		else if (node.getName().compareTo("call") == 0) {
			statement = new CallStatement(node);
		}
		else if (node.getName().compareTo("choose") == 0) {
			statement = new ChooseStatement(node);
		}
		else if (node.getName().compareTo("continue") == 0) {
			statement = new ContinueStatement(node);
		}
		else if (node.getName().compareTo("decl") == 0) {
			statement = new DeclarationStatement(node);
		}
		else if (node.getName().compareTo("do") == 0) {
			statement = new DoStatement(node);
		}
		else if (node.getName().compareTo("return") == 0) {
			statement = new ReturnStatement(node);
		}
		else if (node.getName().compareTo("scope") == 0) {
			statement = new ScopeStatement(node);
		}
		else if (node.getName().compareTo("set") == 0) {
			statement = new SetStatement(node);
		}
		else{
			//ERROR
			ErrorHandler.error(node, this.getClass().toString()
					+ " unknown statement: " + node.getName());
		}
	}

}
