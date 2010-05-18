package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

public class AbstractSyntaxTree {
	Module root;
	public AbstractSyntaxTree(DomNode node) {
		root = new Module(node);
	}
}
