package de.fu_berlin.compilerbau.directoryWriter;

import java.io.IOException;

/**
 * As most of the archives have a similar API, it is wrapped in this class.
 * @author rene
 */
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

	/**
	 * Called immediately after creation.
	 * @throws IOException the underlying system threw an exception
	 */
	protected abstract void writeHeader() throws IOException;

	/**
	 * Called for each call to {@link #writeFile(byte[], CharSequence, CharSequence)}.
	 * @param folder Folder to ensure its existence.
	 * @throws IOException the underlying system threw an exception
	 */
	protected abstract void ensureFolder(CharSequence folder) throws IOException;

	/**
	 * Called for each call to {@link #writeFile(byte[], CharSequence, CharSequence)}
	 * after {@link #ensureFolder(CharSequence)} was called.
	 * @param fileName File name to write the header for.
	 * @param folder Folder where the file belongs to.
	 * @param length Total length of the content
	 * @throws IOException the underlying system threw an exception
	 */
	protected abstract void writeFileHeader(CharSequence fileName, CharSequence folder, int length) throws IOException;

	/**
	 * Called for each call to {@link #writeFile(byte[], CharSequence, CharSequence)}
	 * after {@link #writeFileHeader(CharSequence, CharSequence, int)} was called.
	 * @param input Content to write.
	 * @throws IOException the underlying system threw an exception
	 */
	protected abstract void writeFileContent(byte[] input) throws IOException;
	
	/**
	 * Called for each call to {@link #writeFile(byte[], CharSequence, CharSequence)}
	 * after {@link #writeFileContent(byte[])} was called.
	 * @throws IOException IOException the underlying system threw an exception
	 */
	protected abstract void closeFile() throws IOException;
	
}
