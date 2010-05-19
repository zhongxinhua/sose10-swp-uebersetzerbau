package de.fu_berlin.compilerbau.util;

/**
 * @author kijewski
 */
public class PositionString implements CharSequence, StreamPosition {

	private static final long serialVersionUID = 2796008060686529862L;
	
	protected final CharSequence string;
	protected final int start, line, character;
	
	/**
	 * @see #PositionString(CharSequence, StreamPosition)
	 */
	public PositionString(CharSequence string, int character, int line, int start) {
		this.string = string;
		this.character = character;
		this.line = line;
		this.start = start;
	}

	/**
	 * @param string Data string (*should* be immutable, *may not* contain '\r's)
	 */
	public PositionString(CharSequence string, StreamPosition position) {
		this(string, position.getCharacter(), position.getLine(), position.getStart());
	}

	@Override
	public char charAt(int i) {
		return string.charAt(i);
	}

	@Override
	public int length() {
		return string.length();
	}

	@Override
	public CharSequence subSequence(int start, int end) throws IndexOutOfBoundsException {
		final CharSequence sq = string.subSequence(0, end);
		if(start == 0) {
			return new PositionString(sq, this);
		}
		
		final PositionCharacterStream stream = new PositionCharacterStream(new CharSequenceReader(sq));
		for(int i = 0; i < start && stream.hasNext(); ++i) {
			stream.next();
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCharacter() {
		return character;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getStart() {
		return start;
	}
	
	@Override
	public String toString() {
		return string.toString();
	}

}
