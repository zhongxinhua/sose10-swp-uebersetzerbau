 package ourStAX;

import java.io.*;

public class OurStAXFactory {
	
	public static IOurStAX createNewInstance(Reader reader) throws IOException {
		return new OurStAX(reader);
	}
	
}
