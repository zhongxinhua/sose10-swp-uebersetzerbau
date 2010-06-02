package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.java.RuntimeFactory;

class RuntimeFactoryTest {
	
	static final URL RT_JAR;
	static {
		try {
			RT_JAR = new URL("file:///usr/lib/jvm/java-6-openjdk/jre/lib/rt.jar");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException {
		Runtime newRuntime = RuntimeFactory.newRuntime(null, new URL[] {}, RT_JAR);
		System.out.println(newRuntime);
	}

}
