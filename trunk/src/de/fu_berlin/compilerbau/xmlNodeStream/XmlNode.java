package de.fu_berlin.compilerbau.xmlNodeStream;

/**
 * Raw representation of an XML node.
 * 
 * @author rene
 */
public interface XmlNode extends StreamPosition {
	/**
	 * type of node
	 */
	NodeType getType();
	
	/**
	 * set for: <ul>
	 *   <li>NT_TAG (tag)</li>
	 *   <li>NT_END_TAG (tag if not for <tag .../>)</li>
	 *   <li>NT_ATTR (attribute)</li>
	 *   <li>NT_PI (<a href="http://www.w3.org/TR/REC-xml/#NT-PITarget">PI target</a>)</li></ul>
	 * otherwise null or empty
	 */
	String getKey();
	
	/**
	 * set for: <ul>
	 *   <li>NT_TEXT (text content)</li>
	 *   <li>NT_COMMENT (comment)</li>
	 *   <li>NT_ATTR (value)</li>
	 *   <li>NT_PI (processing instruction)</li></ul>
	 * otherwise null or empty
	 */
	String getValue();
}
