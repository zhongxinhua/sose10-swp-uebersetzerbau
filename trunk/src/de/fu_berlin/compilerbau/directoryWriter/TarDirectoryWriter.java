package de.fu_berlin.compilerbau.directoryWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

public class TarDirectoryWriter extends ArchiveDirectoryWriter {

	protected final OutputStream dest;
	protected final HashSet<File> folders = new HashSet<File>();

	public TarDirectoryWriter(OutputStream dest) throws IOException {
		this.dest = dest;
	}

	@Override
	protected void writeFooter() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeHeader() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void closeFile() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeFileContent(byte[] input) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeFileHeader(CharSequence fileName, CharSequence folder, int length) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void ensureFolder(CharSequence folder) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
