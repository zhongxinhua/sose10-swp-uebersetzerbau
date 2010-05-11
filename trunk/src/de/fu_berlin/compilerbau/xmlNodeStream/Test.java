package de.fu_berlin.compilerbau.xmlNodeStream;

import java.io.*;

import de.fu_berlin.compilerbau.xmlNodeStream.impl.XmlNodeStreamFactory;

class Test {
	
	public static void main(String[] args) throws IOException {
		Reader reader = new InputStreamReader(System.in);
		
		XmlNodeStream st = XmlNodeStreamFactory.createNewInstance(reader);
		for(XmlNode node : st) {
			System.out.println(node);
		}
	}
	
}
