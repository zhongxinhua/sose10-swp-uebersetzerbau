package test;

import java.io.*;

import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;
import de.fu_berlin.compilerbau.xmlNodeStream.impl.XmlNodeStreamFactory;

class XmlNodeStreamTest {
	
	public static void main(String[] args) throws IOException {
		Reader reader = new InputStreamReader(System.in);
		
		XmlNodeStream st = XmlNodeStreamFactory.createNewInstance(reader);
		for(XmlNode node : st) {
			System.out.println(node);
		}
	}
	
}
