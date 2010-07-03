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
			return new XmlNodeImpl(parent.getStart(), parent.getLine(), parent.getColumn(), NodeType.NT_ERROR, null, null);
		}
	}
	
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
}
