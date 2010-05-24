package test.DirectoryWriter;

import java.io.FileOutputStream;
import java.io.IOException;

import de.fu_berlin.compilerbau.directoryWriter.ZipDirectoryWriter;

public class ZipDirectoryWriterTest extends Test {
	
	public static void main(String[] args) throws IOException, IllegalAccessException {
		if(args.length == 0) {
			System.err.println("Erwarte Argument für das ZIP!");
			System.exit(1);
		}
		
		ZipDirectoryWriter directoryWriter = new ZipDirectoryWriter(
				new FileOutputStream(args[0]));
		performTest(directoryWriter);
	}
	
}
