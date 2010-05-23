package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

/**
 * <b>*********** Not implemented yet! ***********</b>
 * <p/>
 * {@link ReturnStatement} is a subclass of {@link Statement} representing a
 * &ltreturns/&gt statement forming a node in the parse tree.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltreturns/&gt statement needs following attributes:
 * <ul>
 * <li>...</li>
 * </ul>
 * The &ltreturns/&gt statement has following optional attributes:
 * <ul>
 * <li>...</li>
 * </ul>
 * <p>
 * The &ltreturns/&gt statement body is forbidden to exist.
 * 
 * @author Sam
 * 
 */
public class ReturnStatement extends Statement {

	public ReturnStatement(DomNode child) {
		// TODO Auto-generated constructor stub
	}

	Expression value;
}