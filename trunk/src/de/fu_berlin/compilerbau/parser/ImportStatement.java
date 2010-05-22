package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

/**
 * {@link ImportStatement} is a subclass of {@link Statement} representing a
 * &ltimport/&gt statement forming a node in the parse tree.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltimport/&gt statement needs following attributes:
 * <ul>
 * <li>name - the name of the class to import</li>
 * </ul>
 * The &ltimport/&gt statement has following optional attributes:
 * <ul>
 * <li>TODO:</li>
 * </ul>
 * <p>
 * The &ltimport/&gt body is forbidden to exist.
 * 
 * @author Sam
 * 
 */
public class ImportStatement extends Statement {

	public ImportStatement(DomNode decl) {

		// TODO Auto-generated constructor stub
	}

}
