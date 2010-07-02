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
@SuppressWarnings("serial")
public class Statement extends SyntaxTreeNode {
	public static Statement build(DomNode node) {
		
		if (node.getName().equals("break")) {
			return new BreakStatement(node);
		}
		else if (node.getName().equals("call")) {
			return new CallStatement(node);
		}
		else if (node.getName().equals("choose")) {
			return new ChooseStatement(node);
		}
		else if (node.getName().equals("continue")) {
			return new ContinueStatement(node);
		}
		else if (node.getName().equals("decl")) {
			return new DeclarationStatement(node);
		}
		else if (node.getName().equals("do")) {
			return new DoStatement(node);
		}
		else if (node.getName().equals("return")) {
			return new ReturnStatement(node);
		}
		else if (node.getName().equals("scope")) {
			return new ScopeStatement(node);
		}
		else if (node.getName().equals("set")) {
			return new SetStatement(node);
		} else
		if (node.getName().equals("foreach")) {
			return new ForEachStatement(node);
		}
		else{
			//ERROR
			ErrorHandler.error(node, "unknown statement: " + node.getName());
			return null;
		}
	}

}
