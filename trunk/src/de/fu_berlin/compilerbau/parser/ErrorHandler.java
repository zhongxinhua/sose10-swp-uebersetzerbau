package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

public class ErrorHandler {
	static void error(DomNode node, String msg) {
		System.err.println("Error: "+msg);
	}
	static void warning(DomNode node, String msg) {
		System.err.println("Warning: "+msg);
	}
}
