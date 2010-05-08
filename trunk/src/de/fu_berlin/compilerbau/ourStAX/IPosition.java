package de.fu_berlin.compilerbau.ourStAX;

public interface IPosition {
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
