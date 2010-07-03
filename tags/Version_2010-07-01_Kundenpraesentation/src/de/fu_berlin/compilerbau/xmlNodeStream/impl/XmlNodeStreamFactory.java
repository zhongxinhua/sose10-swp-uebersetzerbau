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

package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.IOException;
import java.io.Reader;

import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;

/**
 * A factory that creates {@link XmlNodeStream}s.
 * @author rene
 */
public class XmlNodeStreamFactory {
	
	/**
	 * create new instance
	 * @param str Text stream to read from
	 * @return an instance of an XmlNodeStream
	 * @throws IOException the {@link #reader} threw an Exception.
	 */
	public static XmlNodeStream createNewInstance(PositionString str) throws IOException {
		return new XmlNodeStreamImpl(str);
	}
	
	/**
	 * create new instance
	 * @param reader Text stream to read from
	 * @param pos Current position in stream
	 * @return an instance of an XmlNodeStream
	 * @throws IOException the {@link #reader} threw an Exception.
	 */
	public static XmlNodeStream createNewInstance(Reader reader, StreamPosition pos) throws IOException {
		return new XmlNodeStreamImpl(reader, pos);
	}
	
	/**
	 * create new instance
	 * @param reader Text stream to read from
	 * @return an instance of an XmlNodeStream
	 * @throws IOException the {@link #reader} threw an Exception.
	 */
	public static XmlNodeStream createNewInstance(Reader reader) throws IOException {
		return new XmlNodeStreamImpl(reader, PositionBean.ONE_ONE_ONE);
	}
	
}
