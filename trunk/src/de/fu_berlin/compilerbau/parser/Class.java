package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;

public class Class extends ClassOrInterface {
	Class parent;
	List<DeclarationStatement> attributes = new LinkedList<DeclarationStatement>();
	List<Function> functions = new LinkedList<Function>();
	List<Reference> interfaces = new LinkedList<Reference>();
	
	public Class(DomNode node) {
		//name
		if (node.hasAttribute("name") && !node.getAttribute("name").equals("")) {
			name = node.getAttribute("name");
		} else
			ErrorHandler.error(node, "'name' attribute expected");
		
		if (node.hasAttribute("super") && !node.getAttribute("super").equals("")) {
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