package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

/**
 * <b>*********** Not implemented yet! ***********</b>
 * <p/>
 * {@link Statement} is a superclass of all kind of statements forming a node in
 * the parse tree.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * there are following Statements:
 * <ul>
 * <li>{@link ArgumentStatement}</li>
 * <li>{@link AssignStatement}</li>
 * <li>{@link BreakStatement}</li>
 * <li>{@link CallStatement}</li>
 * <li>{@link ChooseStatement}</li>
 * <li>{@link ContinueStatement}</li>
 * <li>{@link DeclarationStatement}</li>
 * <li>{@link DoStatement}</li>
 * <li>{@link ImplementStatement}</li>
 * <li>{@link ImportStatement}</li>
 * <li>{@link ReturnStatement}</li>
 * <li>{@link ScopeStatement}</li>
 * <li>{@link StaticStatement}</li> 
 * </ul>
 * 
 * 
 * @author Sam
 * 
 */
public class Statement {

	public Statement() {

	}

	public Statement(DomNode child) {
		// TODO Auto-generated constructor stub
	}

}
