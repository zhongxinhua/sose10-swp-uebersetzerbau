/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.util;

import java.io.Reader;

/**
 * A {@link Reader} that reads from a {@linl CharSequence}.
 * @author rene
 */
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
