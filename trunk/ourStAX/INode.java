package ourStAX;

public interface INode extends IPosition {
	/**
	 * type of node
	 */
	NodeType getType();
	
	/**
	 * set for:
	 *   NT_TAG (tag)
	 *   NT_END_TAG (tag if not for <tag .../>)
	 *   NT_ATTR (attribute)
	 *   NT_PI (<a href="http://www.w3.org/TR/REC-xml/#NT-PITarget">PI target</a>)
	 * otherwise null or empty
	 */
	String getKey();
	
	/**
	 * set for:
	 *   NT_TEXT (text content)
	 *   NT_COMMENT (comment)
	 *   NT_ATTR (value)
	 *   NT_PI (processing instruction)
	 * otherwise null or empty
	 */
	String getValue();
}
