package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link AbstractSyntaxTree} represents the base class for all further
 * operations considering the building process of a parse tree. The process
 * itself is intended to be recursive, i.e. passing down {@link DomNode} to the
 * corresponding constructors one by one.
 * <p/>
 * When a {@link DomNode} is passed down to a constructor, before this a
 * verification has to take place in order to guarantee the constructor gets it's
 * matching statement. It is the upper class duty to check for correct
 * statements before calling the constructor.
 * </p>
 * The {@link Statement} Class forms an exception, as there is no checking before the constructor is called. 
 * <p/>
 * I.E: before <i>new Module(node)</i> can be called, there has to be a positive
 * test of <i>node.getName().equals("module")</i>
 * 
 * 
 * 
 * @author Sam
 * 
 */

public class AbstractSyntaxTree {
	private Module root;

	/**
	 * default constructor called by startprocess after the DOM creation is
	 * done, passing the created {@link DomNode} representing the whole program.
	 * 
	 * @param node
	 *            {@link DomNode} representing the whole program
	 */
	public AbstractSyntaxTree(DomNode node) {
		if (node.getName().compareTo("module") == 0) {
			root = new Module(node);
		} else {
			ErrorHandler.error(node, "'module' expected");
		}		
	}
	
	public Module getRoot() { return root; }
}
