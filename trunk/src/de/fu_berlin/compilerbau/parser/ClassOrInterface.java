package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

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
