package ourStAX;

import java.io.*;
import java.util.*;

public class OurStAX extends Position implements IOurStAX, Closeable {
	
	private final BufferedReader reader;
	
	public OurStAX(Reader reader) throws IOException {
		super(0,0,0);
		if(reader instanceof BufferedReader) {
			this.reader = (BufferedReader)reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
	}
	
	public OurStAX(File input) throws IOException {
		this(new FileReader(input));
	}
	
	public OurStAX(InputStream input) throws IOException {
		this(new InputStreamReader(input));
	}
	
	public Iterator<INode> iterator() {
		return new NodeIterator(this);
	}
	
	public void close() throws IOException {
		reader.close();
	}
	
	@Override
	public String toString() {
		return reader.toString() + super.toString();
	}
	
	protected Node next = null;
	
	boolean hasNext() throws IOException {
		if(next == null) {
			fetchNext();
			return next != null;
		} else {
			return true;
		}
	}
	
	Node getNext() throws IOException, NoSuchElementException {
		if(hasNext()) {
			Node next = this.next;
			this.next = null;
			return next;
		} else {
			throw new NoSuchElementException();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////
	// ACTUAL IMPLEMENTATION
	/////////////////////////////////////////////////////////////////////////////
	
	protected void fetchNext() throws IOException {
		// TODO
	}
	
}
