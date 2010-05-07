package ourStAX;

import java.io.*;

class Test {
	
	public static void main(String[] args) throws IOException {
		Reader reader = new InputStreamReader(System.in);
		
		IOurStAX st = OurStAXFactory.createNewInstance(reader);
		for(INode node : st) {
			System.out.println(node);
		}
	}
	
}
