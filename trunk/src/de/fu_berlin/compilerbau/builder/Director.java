package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;
import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;

public class Director {
	
	public static void build(Builder builder, DirectoryWriter directoryWriter)
			throws IOException {
		
		final Module module = builder._astree.getRoot();
		
		// replace dots with slashes
		final String moduleRoot = "/" + Pattern.compile("\\.").
				matcher(module.getName()).replaceAll("/");
		
		// write each interface
		for(Interface interfaze : module.getInterfaces()) {
			final OutputStream file;
			try {
				file = directoryWriter.openFile(interfaze.getName(), moduleRoot);
			} catch(IllegalAccessException e) {
				throw new RuntimeException("This cannot happen.", e);
			}
			final OutputStreamWriter writer = new OutputStreamWriter(file);
			
			builder.setCode(writer);
			builder.buildInterface(interfaze);
			builder.setCode(null);
			
			writer.close();
		}
		
		// write each class
		for(Class clazz : module.getClasses()) {
			final OutputStream file;
			try {
				file = directoryWriter.openFile(clazz.getName(), moduleRoot);
			} catch(IllegalAccessException e) {
				throw new RuntimeException("This cannot happen.", e);
			}
			final OutputStreamWriter writer = new OutputStreamWriter(file);
			
			builder.setCode(writer);
			builder.buildClass(clazz);
			builder.setCode(null);
			
			writer.close();
		}
		
	}
}
