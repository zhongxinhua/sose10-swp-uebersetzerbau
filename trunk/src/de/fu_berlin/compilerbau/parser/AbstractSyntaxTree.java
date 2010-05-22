package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;

/**
 * {@link AbstractSyntaxTree} represents the base class for all further operations
 * considering the building process of a parse tree. The process itself is
 * intended to be recursive, i.e. passing down {@link DomNode} to the corresponding
 * constructors.
 * <p/>
 * Each constructor for itself, beginning with {@link Module}, is in duty to verify
 * the correctness of the passed over {@link DomNode} by matching the name and
 * furthermore the existence of needed attributes.
 * 
 * @author Sam
 * 
 */

public class AbstractSyntaxTree {
	Module root;

	/**
	 * default constructor called by startprocess after the DOM creation is done,
	 * passing the created {@link DomNode} representing the whole program.
	 * 
	 * @param node
	 *            {@link DomNode} representing the whole program
	 */
	public AbstractSyntaxTree(DomNode node) {
		root = new Module(node);
	}
}
