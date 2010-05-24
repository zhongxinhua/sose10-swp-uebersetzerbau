package de.fu_berlin.compilerbau.directoryWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;

public class PhysicalDirectoryWriter implements DirectoryWriter {
	
	@Override
	public void writeFile(byte[] input, CharSequence fileName, CharSequence folder)
			throws IOException, IllegalAccessException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void close() throws IOException {
		// ignore
	}

	@Override
	public OutputStream openFile(CharSequence fileName, CharSequence folder) throws IOException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeFile(int contentLength, InputStream stream, CharSequence fileName,
			CharSequence folder) throws IOException, IllegalAccessException, SyncFailedException {
		// TODO Auto-generated method stub
	}
	
}
