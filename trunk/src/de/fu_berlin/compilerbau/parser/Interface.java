package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

public class Interface extends ClassOrInterface {
	List<Function> functions;
	
	public Interface(DomNode node) {

		for (DomNode child : node.getChilds()) {
			if (child.getName().compareTo("function")==0) {
				Function tmp = new Function(child);

				functions.add(tmp);

				if (tmp.hasBody()) {
					ErrorHandler.error(node, "'interface' has to have empty function declarations!");
				}
			}
		}

	}

	
}