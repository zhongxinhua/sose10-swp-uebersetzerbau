package de.fu_berlin.compilerbau.xmlNodeStream;

/**
 * Stores the position in stream of a read element.
 * 
 * @author rene
 */
public interface StreamPosition {
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
