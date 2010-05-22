package de.fu_berlin.compilerbau.parser;
import java.util.LinkedList;
import java.util.List;
import de.fu_berlin.compilerbau.dom.DomNode;
/**
 * {@link Class} is a subclass of {@link ClassOrInterface} representing a &ltclass/&gt statement.<br>
 * It may consist of:
 * <ul>
 * <li>at most one superclass declaration</li>
 * <li>arbitrary interface implementations</li>
 * <li>arbitrary attribute definitions</li>
 * <li>arbitrary function definitions</li>
 * </ul>
 * 
 * @author Sam
 *
 */
public class Class extends ClassOrInterface {
	Class parent;
	List<Reference> interfaces = new LinkedList<Reference>();
	List<DeclarationStatement> attributes = new LinkedList<DeclarationStatement>();
	List<Function> functions = new LinkedList<Function>();
	
	
	public Class(DomNode node) {
		//name
		if (node.hasAttribute("name") && node.getAttributeValue("name").length()>0) {
			name = node.getAttributeValue("name");
		} else
			ErrorHandler.error(node, "'name' attribute expected");
		
		if (node.hasAttribute("super") && !node.getAttributeValue("super").equals("")) {
			//TODO parent ermitteln 
			//parent = node.getAttribute("super");
		} 
			
		for (DomNode child : node.getChilds()) {
			if(child.getName().equals("decl")){
				attributes.add(new DeclarationStatement(child));
			}
			if(child.getName().equals("function")){
				functions.add(new Function(child));
			}
			if(child.getName().equals("implements")){
				interfaces.add(new Reference(child));
			}

		}
	}


}