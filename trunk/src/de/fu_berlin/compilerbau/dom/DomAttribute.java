package de.fu_berlin.compilerbau.dom;

/**
 * DomAttribute.java
 * <p>
 * Representation of an Attribute, which can be used in DomNodes.
 * 
 * @see DomNode.java
 * 
 * @author stefan
 */

public interface DomAttribute {
	
	/*
	 * The Name of the Attribute
	 * 
	 * @return name of the attribute or null
	 */
	String getName();
	
	/*
	 * The Value of the Attribute
	 * <p>
	 * Hint: Of course, the value is not escaped anymore
	 * 
	 * @return value of the attribute or null
	 */
	String getValue();
}
