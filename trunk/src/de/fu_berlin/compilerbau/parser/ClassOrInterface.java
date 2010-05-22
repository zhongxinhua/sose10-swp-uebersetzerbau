package de.fu_berlin.compilerbau.parser;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
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
 * 
 */
public class ClassOrInterface {
	//Wird in Class oder Interface belegt
	PositionString name;
	
	//TODO:Wozu diese Liste? 
	List<Interface> interfaces;
	/**
	 * Static method awaits a {@link DomNode} representing &ltclass/&gt or &ltinterface/&gt statement.<br>
	 * Calling the  correct constructor it returns a {@link Class} or {@link Interface} object.
	 * @param node {@link DomNode} representing &ltclass/&gt or &ltinterface/&gt statement
	 * @return {@link Class} or {@link Interface} object
	 */
	static ClassOrInterface build(DomNode node) {
		if (node.getName().equals("class")) {
			return new Class(node);
		} else if (node.getName().equals("interface")) {
			return new Interface(node);
		} else
			ErrorHandler.error(node, "'module' expected");
		return null;
	}

}
