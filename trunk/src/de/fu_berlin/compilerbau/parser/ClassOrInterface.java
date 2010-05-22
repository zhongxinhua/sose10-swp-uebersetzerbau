package de.fu_berlin.compilerbau.parser;

import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * {@link ClassOrInterface} is a superclass for {@link Class} or
 * {@link Interface} objects representing &ltclass/&gt or &ltinterface/&gt statements.
 * <p>
 * 
 * @author Sam
 * 
 */
public class ClassOrInterface {
	static ClassOrInterface build(DomNode node) {
		if (node.getName().equals("class")) {
			return new Class(node);
		} else if (node.getName().equals("interface")) {
			return new Interface(node);
		} else
			ErrorHandler.error(node, "'module' expected");
		return null;
	}

	PositionString name;
	List<Interface> interfaces;
}
