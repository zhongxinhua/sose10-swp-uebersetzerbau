package de.fu_berlin.compilerbau.dom;

/**
 * DomNode.java
 * <p>
 * Representation of an Node for the DOM-Tree.
 * 
 * @author stefan
 */

import java.util.List;

public interface DomNode {
	
	/**
	 * Check if this Node is a Leaf.
	 * <p>
	 * If a Node is Leaf, it does not contain any child's
	 * 
	 * @return True, if Node is a Leaf
	 */
	boolean isLeaf();
	
	/**
	 * Check if this Node is not a Leaf
	 * <p>
	 * If a Node is not a Leaf, it does contain at least one child
	 * 
	 * @return True, if Node is not a Leaf
	 */
	boolean isNode();
	
	/**
	 * 
	 * @return
	 */
	List<DomNode> getChilds();
	
	/**
	 * 
	 * @return
	 */
	DomNode getParent();

	/**
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * 
	 * @return
	 */
	List<DomAttribute> getAttributes();	
}
