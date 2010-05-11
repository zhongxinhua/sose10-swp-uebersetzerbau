 package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.*;

import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;

public class XmlNodeStreamFactory {
	
	public static XmlNodeStream createNewInstance(Reader reader) throws IOException {
		return new XmlNodeStreamImpl(reader);
	}
	
}
