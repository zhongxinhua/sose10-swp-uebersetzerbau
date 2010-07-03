package test.DirectoryWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;

abstract class Test {
	
	static void performTest(DirectoryWriter directoryWriter) throws IOException, IllegalAccessException {
		OutputStream stream = directoryWriter.openFile("openFile.test", "openFile");
		PrintWriter writer = new PrintWriter(stream);
		writer.println("Zeile 1");
		writer.println("Zeile 2");
		writer.println("Zeile 3");
		writer.close();
		
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append("Zeile 1\n");
		stringBuffer.append("Zeile 2\n");
		stringBuffer.append("Zeile 3\2");
		byte[] bytes = stringBuffer.toString().getBytes();
		directoryWriter.writeFile(bytes, "buffer.test", "writeFile");
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		directoryWriter.writeFile(bytes.length, inputStream, "stream.test", "writeFile");
		
		directoryWriter.close();
	}
	
}
