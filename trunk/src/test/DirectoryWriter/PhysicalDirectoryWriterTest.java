package test.DirectoryWriter;

import java.io.File;
import java.io.IOException;

import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;
import de.fu_berlin.compilerbau.directoryWriter.PhysicalDirectoryWriter;

public class PhysicalDirectoryWriterTest extends Test {
	
	public static void main(String[] args) throws IOException, IllegalAccessException {
		if(args.length == 0) {
			System.err.println("Erwarte Argument für den Root!");
			System.exit(1);
		}
		
		DirectoryWriter directoryWriter = new PhysicalDirectoryWriter(new File(args[0]));
		performTest(directoryWriter);
	}
	
}
