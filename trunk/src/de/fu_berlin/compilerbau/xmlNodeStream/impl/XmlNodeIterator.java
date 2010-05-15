package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.*;
import java.util.*;

import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;

/**
 * A class to match the {@link java.util.Iterator} interface.
 * @author rene
 */
class XmlNodeIterator implements Iterator<XmlNode> {
	
	protected final XmlNodeStreamImpl parent;
	
	XmlNodeIterator(XmlNodeStreamImpl parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return parent.hasNext();
		} catch(IOException e) {
			e.printStackTrace(); // could be re-thrown as a RuntimeException ...
			return false;
		}
	}
	
	/*
	 * Q: What about the IllegalStateExceptions?
	 * A: As an IllegalStateException can only be thrown if the implementation of xmlNodeStreamImpl.hasNext() is
	 *    flawed, we can ignore it in here.
	 */
	
	@Override
	public XmlNodeImpl next() throws NoSuchElementException {
		try {
			return parent.getNext();
		} catch(IOException e) {
			e.printStackTrace(); // could be re-thrown as a RuntimeException ...
			return new XmlNodeImpl(parent.getStart(), parent.getLine(), parent.getCharacter(), NodeType.NT_ERROR, null, null);
		}
	}
	
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
}
