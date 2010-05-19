package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

public class Module {
	public Module(DomNode node) {
		declarations = new LinkedList<ClassOrInterface>();
		if (node.getName().equals("module")) {
			if (node.hasAttribute("name")
					&& node.getAttributeValue("name").length()>0) {
				name = node.getAttributeValue("name");
				for (DomNode child : node.getChilds()) {
					ClassOrInterface c = ClassOrInterface.build(child);
					if (c != null)
						declarations.add(c);
				}
			} else
				ErrorHandler.error(node, "'name' attribute expected");
		} else
			ErrorHandler.error(node, "'module' expected");
	}

	PositionString name;
	// List<Type> imports;
	List<ClassOrInterface> declarations;
}
