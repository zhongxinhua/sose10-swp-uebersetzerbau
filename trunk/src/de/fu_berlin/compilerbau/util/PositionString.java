package de.fu_berlin.compilerbau.util;

/**
 * @author kijewski
 */
public class PositionString extends PositionBean implements CharSequence {

	private static final long serialVersionUID = 2796008060686529862L;
	
	protected final CharSequence string;

	/**
	 * @param string Data string (*should* be immutable)
	 */
	public PositionString(CharSequence string, StreamPosition position) {
		super(position);
		this.string = string;
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

}
