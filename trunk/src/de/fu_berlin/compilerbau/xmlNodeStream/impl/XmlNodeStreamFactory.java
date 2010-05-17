 package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.IOException;
import java.io.Reader;

import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;

/**
 * A factory that creates {@link XmlNodeStream}s.
 * @author rene
 */
public class XmlNodeStreamFactory {
	
	/**
	 * create new instance
	 * @param reader Text stream to read from
	 * @return an instance of an XmlNodeStream
	 * @throws IOException the {@link #reader} throw an Exception.
	 */
	public static XmlNodeStream createNewInstance(Reader reader) throws IOException {
		return new XmlNodeStreamImpl(reader);
	}
	
}