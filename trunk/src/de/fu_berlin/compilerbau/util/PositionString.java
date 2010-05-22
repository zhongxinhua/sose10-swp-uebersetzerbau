package de.fu_berlin.compilerbau.util;

/**
 * @author kijewski
 */
public class PositionString extends PositionBean implements CharSequence, Comparable<Object> {

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
	public PositionString subSequence(int start, int end) throws IndexOutOfBoundsException {
		if(start < 0 || end > length()) {
			throw new IndexOutOfBoundsException();
		}
		
		final StreamPosition pos;
		if(start == 0) {
			pos = this;
		} else {
			final PositionCharacterStream stream = new PositionCharacterStream(new CharSequenceReader(string));
			for(int i = 0; i < start; ++i) {
				stream.next();
			}
			pos = stream;
		}
		return new PositionString(string.subSequence(start, end), pos);
	}

	@Override
	public int compareTo(Object o) throws NullPointerException, ClassCastException {
		if(o == this) {
			return 0;
		} else if(o == null) {
			throw new NullPointerException("o is null!");
		} else if(o instanceof CharSequence) {
			final CharSequence r = (CharSequence)o;
			int i = 0;
			final int lLen = length(), rLen = r.length();
			for(;;) {
				if(lLen <= i) {
					return rLen <= i ? 0 : -1;
				} else if(rLen <= i) {
					return +1;
				}
				final int v = charAt(i) -r.charAt(i);
				if(v != 0) {
					return v;
				}
			}
		} else {
			throw new ClassCastException("Cannot compare " + o.getClass().getName() +
					" against " + getClass().getName());
		}
	}

}
