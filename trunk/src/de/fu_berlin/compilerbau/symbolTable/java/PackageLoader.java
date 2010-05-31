package de.fu_berlin.compilerbau.symbolTable.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Basically it unhides super methods. :)
 * @author kijewski
 */
class PackageLoader extends URLClassLoader {
	
	protected static URL[] concatUrls(URL rtJar, URL[] classpath) {
		final URL[] result = new URL[classpath.length + 1];
		result[0] = rtJar;
		System.arraycopy(classpath, 0, result, 1, classpath.length);
		return result;
	}
	
	public PackageLoader(URL rtJar, URL[] classpath) throws IOException {
		super(concatUrls(rtJar, classpath));
	}
	
	@Override
	public Package[] getPackages() {
		return super.getPackages();
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}
}
