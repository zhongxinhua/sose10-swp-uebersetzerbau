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
	 * otherwise null or empty
	 */
	String getKey();
	
	/**
	 * set for:
	 *   NT_TEXT (text content)
	 *   NT_COMMENT (comment)
	 *   NT_ATTR (value)
	 * otherwise null or empty
	 */
	String getValue();
}
