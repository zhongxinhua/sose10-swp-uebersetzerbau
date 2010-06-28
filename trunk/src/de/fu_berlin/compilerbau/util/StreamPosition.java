package de.fu_berlin.compilerbau.util;

import java.io.Serializable;

/**
 * Stores the position in stream of a read element.
 * @author rene
 */
public interface StreamPosition extends Serializable {
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
	int getColumn();
}
