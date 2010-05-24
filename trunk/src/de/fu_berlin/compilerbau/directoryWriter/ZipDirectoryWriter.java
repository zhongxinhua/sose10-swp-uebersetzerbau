package de.fu_berlin.compilerbau.directoryWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDirectoryWriter extends ArchiveDirectoryWriter {

	protected final ZipOutputStream zip;

	public ZipDirectoryWriter(OutputStream dest) throws IOException {
		zip = new ZipOutputStream(dest);
	}
	
	@Override
	protected void closeFile() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void ensureFolder(CharSequence folder) throws IOException {
		// ignore
	}
	
	@Override
	protected void writeFileHeader(CharSequence fileName, CharSequence folder, int length) throws IOException {
		final File destFile = new File(new File(folder.toString()), fileName.toString());
		zip.putNextEntry(new ZipEntry(destFile.toString()));
	}
	
	@Override
	protected void writeFileContent(byte[] input) throws IOException {
		final WritableByteChannel outChannel = Channels.newChannel(zip);
		outChannel.write(ByteBuffer.wrap(input));
		// outChannel.close(); // closes stream just as well
	}
	
	@Override
	protected void writeFooter() throws IOException {
		zip.closeEntry();
	}
	
	@Override
	protected void writeHeader() throws IOException {
		// ignore
	}

	@Override
	public void close() throws IOException {
		zip.close();
	}
	
}
