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
