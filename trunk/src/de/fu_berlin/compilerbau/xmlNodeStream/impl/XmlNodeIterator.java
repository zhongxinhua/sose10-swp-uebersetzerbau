package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import java.io.*;
import java.util.*;

import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;

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
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public XmlNodeImpl next() throws NoSuchElementException {
		try {
			return parent.getNext();
		} catch(IOException e) {
			e.printStackTrace();
			return new XmlNodeImpl(parent.getStart(), parent.getLine(), parent.getCharacter(), NodeType.NT_ERROR, null, null);
		}
	}
	
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
