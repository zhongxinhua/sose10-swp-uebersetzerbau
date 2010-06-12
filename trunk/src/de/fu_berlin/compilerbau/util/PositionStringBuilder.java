package de.fu_berlin.compilerbau.util;

public class PositionStringBuilder extends PositionBean
		implements Appendable, StreamPosition {

	private static final long serialVersionUID = -5035361425725441705L;
	
	public PositionStringBuilder(int start, int line, int character) {
		super(start, line, character);
	}
	
	public PositionStringBuilder(StreamPosition pos) {
		super(pos);
	}
	
	protected final StringBuilder builder = new StringBuilder();

	@Override
	public Appendable append(CharSequence sq) {
		builder.append(sq);
		return this;
	}

	@Override
	public Appendable append(char c) {
		builder.append(c);
		return this;
	}

	@Override
	public Appendable append(CharSequence sq, int start, int end) {
		builder.append(sq, start, end);
		return this;
	}
	
	public PositionString toPositionString() {
		return new PositionString(builder.toString(), this);
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

}
