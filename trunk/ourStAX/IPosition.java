package ourStAX;

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
