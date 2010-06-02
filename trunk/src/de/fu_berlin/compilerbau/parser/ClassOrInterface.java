package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * {@link ClassOrInterface} is a superclass for {@link Class} or
 * {@link Interface} objects representing &ltclass/&gt or &ltinterface/&gt
 * statements.
 * <p>
 * Offering the static method build(DomNode node) it returns the correct object
 * corresponding to program code declaration or throws an error.
 * 
 * @author Sam
 * @see {@link Class}
 * @see {@link Interface}
 * 
 */
public abstract class ClassOrInterface extends SyntaxTreeNode {
	//Wird in Class oder Interface belegt
	protected PositionString name;
	/**
	 * Static method awaits a {@link DomNode} representing &ltclass/&gt or &ltinterface/&gt statement.<br>
	 * Calling the  correct constructor it returns a {@link Class} or {@link Interface} object.
	 * @param node {@link DomNode} representing &ltclass/&gt or &ltinterface/&gt statement
	 * @return {@link Class} or {@link Interface} object
	 * @deprecated should be deleted with confirmation of Markus
	 */
	static ClassOrInterface build(DomNode node) {
		if (node.getName().equals("class")) {
			return new Class(node);
		} else if (node.getName().equals("interface")) {
			return new Interface(node);
		} else
			ErrorHandler.error(node, "'calss' or 'interface' expected");
		return null;
	}
	// BEGIN get-Methoden für Builder
	public PositionString getName() {
		return name;
	}
	// END get-Methoden für Builder
}
