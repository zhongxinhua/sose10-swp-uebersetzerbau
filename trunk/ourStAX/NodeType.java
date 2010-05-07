package ourStAX;

public enum NodeType {
	/**
	 * an error occurred (wrong data or broken input)
	 */
	NT_ERROR,
	
	/**
	 * text node / CDATA section
	 */
	NT_TEXT,
	
	/**
	 * comment node
	 */
	NT_COMMENT,
	
	/**
	 * an XML tag
	 */
	NT_TAG,
	
	/**
	 * end of an XML tag
	 */
	NT_END_TAG,
	
	/**
	 * attribute node
	 */
	NT_ATTR
}
