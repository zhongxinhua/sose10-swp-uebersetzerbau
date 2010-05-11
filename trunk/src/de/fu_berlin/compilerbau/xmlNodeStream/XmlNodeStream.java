package de.fu_berlin.compilerbau.xmlNodeStream;

import java.io.*;

/**
 * Reads {@link XmlNode XmlNodes} node by node from streams.
 * 
 * @author rene
 */
public interface XmlNodeStream extends Iterable<XmlNode>, StreamPosition, Closeable {
}
