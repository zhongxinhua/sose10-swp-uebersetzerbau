package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

/**
 * {@link ImplementStatement} is a subclass of {@link Statement} representing a
 * &ltimplement/&gt statement forming a node in the parse tree.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltimplement/&gt statement needs following attributes:
 * <ul>
 * <li>name - the name of the interface to implement</li>
 * </ul>
 * The &ltimplement/&gt statement has following optional attributes:
 * <ul>
 * <li>TODO:</li>
 * </ul>
 * <p>
 * The &ltimplement/&gt body is forbidden to exist.
 * 
 * @author Sam
 * 
 */
public class ImplementStatement extends Statement {

	public ImplementStatement(DomNode child) {
		// TODO Auto-generated constructor stub
	}

}
