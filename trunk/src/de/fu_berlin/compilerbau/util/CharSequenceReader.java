package de.fu_berlin.compilerbau.util;

import java.io.Reader;

public class CharSequenceReader extends Reader {
	
	protected final CharSequence data;
	protected int position = 0;

	public CharSequenceReader(CharSequence data) {
		this.data = data;
	}

	@Override
	public void close() {
		// void
	}

	@Override
	public int read(char[] cbuf, int off, int len) {
		final int MAX_LEN = data.length();
		if(MAX_LEN == position) {
			return -1;
		}
		len = Math.min(len, MAX_LEN - position);
		for(int i = 0; i < len; ++i) {
			cbuf[i+off] = data.charAt(position++);
		}
		return len;
	}

}
