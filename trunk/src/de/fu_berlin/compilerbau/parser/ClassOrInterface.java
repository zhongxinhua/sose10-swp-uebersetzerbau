package de.fu_berlin.compilerbau.parser;
import java.util.List;
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
 * 
 */
public abstract class ClassOrInterface {
	//Wird in Class oder Interface belegt
	protected PositionString name;
	
	//TODO:Wozu diese Liste? 
	List<Interface> interfaces;
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

	public PositionString getName() {
		return name;
	}
}
