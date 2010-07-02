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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * An {@link Iterator iterator} that reads from a {@link Reader} and
 * knows how many characters it read.
 * @author Kijewski
 */
public class PositionCharacterStream extends PositionBean
		implements Iterator<Character>, StreamPosition, Closeable {

	private static final long serialVersionUID = 2988361231152015517L;
	
	protected final Reader reader;

	private Stack<Character> nextChar = new Stack<Character>();
	
	protected static final StreamPosition DEFAULT_POSITION = PositionBean.ONE_ONE_ONE;
	
	public PositionCharacterStream(Reader reader, StreamPosition start) {
		super(start);
		if(reader instanceof BufferedReader) {
			this.reader = reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
	}
	
	public PositionCharacterStream(PositionString str) {
		this(new CharSequenceReader(str), str);
	}
	
	public PositionCharacterStream(CharSequence str, StreamPosition start) {
		this(new CharSequenceReader(str), start);
	}
	
	@Override
	public String toString() {
		return "[" + line + ":" + character + "=@" + start + "]";
	}
	
	/**
	 * Pushes a character to be read by {@link #next()}.
	 * @param c character to push
	 */
	public void pushback(Character c) {
		nextChar.push(c);
	}
	
	/**
	 * @see #pushback(Character)
	 */
	public void pushback(char c) {
		pushback(Character.valueOf(c));
	}
	
	protected Character primitiveRead() throws IOException {
		final int result = reader.read();
		if(result < 0) {
			return null;
		}
		
		++this.start;
		++this.character;
		if(result == '\n' || result == '\r') {
			this.character = DEFAULT_POSITION.getColumn();
			++this.line;
			if(result == '\r') {
				final int next = reader.read();
				if(next >= 0) { // ignore EOF
					++this.start;
					pushback((char)next);
				}
			}
		}
		
		return Character.valueOf((char)result);
	}

	@Override
	public boolean hasNext() {
		if(!nextChar.isEmpty()) {
			return true;
		}
		final Character result;
		try {
			result = primitiveRead();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		if(result == null) {
			return false;
		}
		pushback(result);
		return true;
	}

	@Override
	public Character next() throws NoSuchElementException {
		if(hasNext()) { // no character in chain
			return nextChar.pop();
		} else { // use enchained character
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

}
