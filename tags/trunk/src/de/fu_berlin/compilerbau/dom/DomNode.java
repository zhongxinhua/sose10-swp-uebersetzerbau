package de.fu_berlin.compilerbau.dom;

/**
 * DomNode.java
 * <p>
 * Representation of an Node for the DOM-Tree.
 * 
 * @author stefan
 */

import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public interface DomNode extends StreamPosition {
	
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
	PositionString getName();
	
	/**
	 * 
	 * @return
	 */
	List<DomAttribute> getAttributes();
	
	PositionString getAttribute(String attributeName);
	
	/**
	 * @param attributeName - The Name of the Attribute
	 * @return The Value for the Attribute attributeName or "" if there is no such Attribute
	 */	
	String getAttributeValue(String attributeName);
	
	/**
	 * 
	 * @param attributeName - The Name of the Attribute
	 * @return true, if there is an Attribute with the Name attributeName
	 */
	boolean hasAttribute(String attributeName);
}
