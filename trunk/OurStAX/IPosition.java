package OurStAX;

public interface IPosition {
	/**
	 * set for:
	 *   NT_TEXT (text content)
	 *   NT_COMMENT (comment)
	 *   NT_ATTR (value)
	 * otherwise null or empty
	 */
	String getValue();
	
	/**
	 * n'th character in data stream
	 */
	int getStart();
	
	/**
	 * n'th line in data stream
	 */
	int getLine();
	
	/**
	 * n'th character of line
	 */
	int getCharacter();
}
