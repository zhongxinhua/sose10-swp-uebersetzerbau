package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;

public class Interface extends ClassOrInterface {
	public Interface(DomNode node) {

		for (DomNode child : node.getChilds()) {
			if (child.getName().equals("function")) {
				Function tmp = new Function(child);

				functions.add(tmp);

				if (tmp.hasBody()) {
					ErrorHandler.error(node, "'interface' has to have empty function declarations!");
				}
			}
		}

	}

	List<Function> functions;
}