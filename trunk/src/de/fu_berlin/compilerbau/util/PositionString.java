package de.fu_berlin.compilerbau.util;

/**
 * @author kijewski
 */
public class PositionString extends PositionBean implements CharSequence, Comparable<CharSequence> {

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
	public String toString() {
		return string.toString();
	}

	@Override
	public PositionString subSequence(int start, int end) throws IndexOutOfBoundsException {
		if(start < 0 || end > length()) {
			throw new IndexOutOfBoundsException();
		}
		
		final StreamPosition pos;
		if(start == 0) {
			pos = this;
		} else {
			final PositionCharacterStream stream = new PositionCharacterStream(string, this);
			for(int i = 0; i < start; ++i) {
				stream.next();
			}
			pos = stream;
		}
		return new PositionString(string.subSequence(start, end), pos);
	}

	@Override
	public int compareTo(CharSequence right) throws NullPointerException {
		if(right == this) {
			return 0;
		} else if(right == null) {
			throw new NullPointerException("o is null!");
		}
		
		final int lLen = length(), rLen = right.length();
		for(int i = 0; /*void*/; ++i) {
			if(lLen <= i) {
				return rLen <= i ? 0 : -1;
			} else if(rLen <= i) {
				return +1;
			}
			
			final int v = charAt(i) - right.charAt(i);
			if(v != 0) {
				return v;
			}
		}
	}
	
	@Override
	public boolean equals(Object right) throws ClassCastException {
		if(right == null) {
			return false;
		} else if(right instanceof ClassCastException) {
			return compareTo((CharSequence)right) == 0;
		} else {
			throw new ClassCastException("Cannot compare " + right.getClass().getName() +
					" against " + getClass().getName());
		}
	}

}
