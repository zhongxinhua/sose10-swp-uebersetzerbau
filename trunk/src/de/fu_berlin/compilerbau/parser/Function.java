package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;

public class Function {
	String name;
	Type returns;
	
	//default: NO
	boolean isFinal = false;
	//default: NO
	boolean isStatic = false;
	
	List<DeclarationStatement> arguments;
	List<Statement> body;
	
	public Function(DomNode node) {
		//TODO: Eintrag in Symboltabelle
		//
		if (node.hasAttribute("name") && !node.getAttribute("name").equals("")) {
			name = node.getAttribute("name");
		} else
			ErrorHandler.error(node, "'name' attribute expected at function head");
		
		if (node.hasAttribute("returns") && !node.getAttribute("returns").equals("")) {
			returns = Type.get(node.getAttribute("returns"));
		} 
		
		/*TODO:	SPEZIFIKATION? KEIN "RETURNS" -> Type = Void??
		 else
			ErrorHandler.error(node, "'returns' attribute expected at function head");
		*/
		
		if (node.hasAttribute("static") && !node.getAttribute("static").equals("")) {
			if(node.getAttribute("static").equals("yes")){
				isStatic = true;
			}
		} 
		
		if (node.hasAttribute("final") && !node.getAttribute("final").equals("")) {
			if(node.getAttribute("final").equals("yes")){
				isFinal = true;
			}
		}
		
		//PARAMETER?
		for (DomNode child : node.getChilds()) {
			if(child.getName().equals("arguments")){
				for (DomNode decl : child.getChilds()) {
					arguments.add(new DeclarationStatement(decl));
				}
			}
			body.add(new Statement(child));

		}
		
		
		
	}
	public boolean hasBody() {
		
		return !body.isEmpty();
	}
}