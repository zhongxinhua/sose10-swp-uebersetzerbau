package ourStAX;

import java.io.*;
import java.util.*;

class NodeIterator implements Iterator<INode> {
	protected final OurStAX parent;
	
	NodeIterator(OurStAX parent) {
		this.parent = parent;
	}
	
	public boolean hasNext() {
		try {
			return parent.hasNext();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Node next() throws NoSuchElementException {
		try {
			return parent.getNext();
		} catch(IOException e) {
			e.printStackTrace();
			return new Node(parent.getStart(), parent.getLine(), parent.getCharacter(), NodeType.NT_ERROR, null, null);
		}
	}
	
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
