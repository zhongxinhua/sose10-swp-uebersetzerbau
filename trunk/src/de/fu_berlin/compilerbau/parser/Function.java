package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

public class Function {
	PositionString name;
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
		if (node.hasAttribute("name") && node.getAttributeValue("name").length()>0) {
			name = node.getAttributeValue("name");
		} else
			ErrorHandler.error(node, "'name' attribute expected at function head");
		
		if (node.hasAttribute("returns") && node.getAttributeValue("returns").length()>0) {
			returns = Type.get(node.getAttributeValue("returns"));
		} 
		
		/*TODO:	SPEZIFIKATION? KEIN "RETURNS" -> Type = Void??
		 else
			ErrorHandler.error(node, "'returns' attribute expected at function head");
		*/
		
		if (node.hasAttribute("static") && !node.getAttributeValue("static").equals("")) {
			if(node.getAttributeValue("static").equals("yes")){
				isStatic = true;
			}
		} 
		
		if (node.hasAttribute("final") && !node.getAttributeValue("final").equals("")) {
			if(node.getAttributeValue("final").equals("yes")){
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