package de.fu_berlin.compilerbau.util;

import java.io.Reader;

public class CharSequenceReader extends Reader {
	
	protected final CharSequence data;

	public CharSequenceReader(CharSequence data) {
		this.data = data;
	}

	@Override
	public void close() {
		// void
	}

	@Override
	public int read(char[] cbuf, int off, int len) {
		if(len > data.length()) {
			len = data.length();
		}
		for(int i = 0; i < len; ++i) {
			cbuf[i+off] = data.charAt(i);
		}
		return len;
	}

}
