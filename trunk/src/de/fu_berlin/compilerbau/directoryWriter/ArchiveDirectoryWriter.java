package de.fu_berlin.compilerbau.directoryWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

abstract class ArchiveDirectoryWriter extends UnifyingDirectoryWriter {

	public ArchiveDirectoryWriter() throws IOException {
		writeHeader();
	}

	@Override
	public void writeFile(byte[] input, CharSequence fileName, CharSequence folder)
			throws IOException, IllegalAccessException {
		ensureFolder(folder);
		writeFileHeader(fileName, folder, input.length);
		writeFileContent(input);
		closeFile();
	}
	
	protected abstract void writeFooter() throws IOException;

	protected abstract void writeHeader() throws IOException;
	
	protected abstract void closeFile() throws IOException;

	protected abstract void writeFileContent(byte[] input) throws IOException;

	protected abstract void writeFileHeader(CharSequence fileName, CharSequence folder, int length) throws IOException;

	protected abstract void ensureFolder(CharSequence folder) throws IOException;
	
}
