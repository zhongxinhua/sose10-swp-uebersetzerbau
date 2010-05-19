package de.fu_berlin.compilerbau.util;

import java.io.IOException;

public class PositionStringBuilder implements Appendable, StreamPosition {

	private static final long serialVersionUID = -5035361425725441705L;
	
	protected final int start, line, character;
	
	public PositionStringBuilder(StreamPosition pos) {
		start = pos.getStart();
		line = pos.getLine();
		character = pos.getCharacter();
	}
	
	protected final StringBuilder builder = new StringBuilder();

	@Override
	public Appendable append(CharSequence sq) throws IOException {
		return builder.append(sq);
	}

	@Override
	public Appendable append(char c) throws IOException {
		return builder.append(c);
	}

	@Override
	public Appendable append(CharSequence sq, int start, int end) throws IOException {
		return builder.append(sq, start, end);
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
	
	public PositionString toPositionString() {
		return new PositionString(builder.toString(), this);
	}

}
