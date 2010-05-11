package de.fu_berlin.compilerbau.xmlNodeStream;

/**
 * Content type of the {@link XmlNode}.
 * 
 * @author rene
 */
public enum NodeType {
	/**
	 * an error occurred (wrong data or broken input)
	 */
	NT_ERROR,
	
	/**
	 * text node / <a href="http://www.w3.org/TR/REC-xml/#sec-cdata-sect">CDATA section</a>
	 */
	NT_TEXT,
	
	/**
	 * <a href="http://www.w3.org/TR/REC-xml/#sec-comments">comment node</a>
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
	 * <a href="http://www.w3.org/TR/REC-xml/#attdecls">attribute node</a>
	 */
	NT_ATTR,
	
	/**
	 * <a href="http://www.w3.org/TR/REC-xml/#sec-pi">processing instruction</a>
	 */
	NT_PI
}
