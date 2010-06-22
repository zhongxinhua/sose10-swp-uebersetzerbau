package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.java.RuntimeFactory;

class RuntimeFactoryTest {
	
	static URL RT_JAR;
	static {
			//RT_JAR = new URL("file:///usr/lib/jvm/java-6-openjdk/jre/lib/rt.jar");
			RT_JAR = //new URL("file:///C:/Documents and Settings/markrudo/Desktop/rt.jar");
				ClassLoader.getSystemResource("testfiles/small_runtime.jar");
			if(RT_JAR == null) {
				try {
					RT_JAR = new URL("file:testfiles/small_runtime.jar");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public static void main(String[] args) throws IOException {
		Runtime rt = RuntimeFactory.newRuntime(null, new URL[] {}, RT_JAR);
		System.out.println(rt);
		System.out.println(rt.mangleName("Übersetzerbau"));
		System.out.println(rt.mangleName("übersetzerbau"));
		System.out.println(rt.mangleName("эксперимент"));
	}

}
