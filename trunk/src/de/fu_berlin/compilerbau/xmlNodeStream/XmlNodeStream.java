package de.fu_berlin.compilerbau.xmlNodeStream;

import java.io.*;

import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * Reads {@link XmlNode XmlNodes} node by node from streams.
 * 
 * @author rene
 */
public interface XmlNodeStream extends Iterable<XmlNode>, StreamPosition, Closeable {
}
