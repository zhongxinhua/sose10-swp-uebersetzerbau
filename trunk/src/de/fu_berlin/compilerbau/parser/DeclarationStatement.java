package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

public class DeclarationStatement extends Statement {
	public DeclarationStatement(DomNode decl) {
		// TODO Auto-generated constructor stub
	}
	Type type;
	int dimension;
	boolean isStatic;
	boolean isFinal;
	String name;
	Expression value;
}