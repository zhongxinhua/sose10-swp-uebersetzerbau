package de.fu_berlin.compilerbau.directoryWriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;

/**
 * Interface for directories.
 * @author rene
 */
public interface DirectoryWriter extends Closeable {
	
	/**
	 * Opens a new stream to write to.
	 * It MUST be closed before another file may be opened.
	 * @param fileName The destination file name
	 * @param folder The folder to write the file to. Has to begin with a '/'!
	 * @return stream to write to
	 * @throws IOException The underlying system failed.
	 * @throws IllegalAccessException A file was already opened.
	 */
	OutputStream openFile(CharSequence fileName, CharSequence folder) throws IOException, IllegalAccessException;
	
	/**
	 * Writes a file. See also {@link #openFile(CharSequence, CharSequence)}.
	 * @param contentLength Total length of stream.
	 * 		Method will read at most that many bytes.
	 * 		Stream may not went dry until at least this many bytes were read!
	 * @param stream stream to read from
	 * @throws SyncFailedException stream went dry too early!
	 */
	void writeFile(int contentLength, InputStream stream, CharSequence fileName, CharSequence folder)
			throws IOException, IllegalAccessException, SyncFailedException;

	/**
	 * @see #writeFile(int, InputStream, CharSequence, CharSequence)
	 */
	void writeFile(byte[] input, CharSequence fileName, CharSequence folder)
			throws IOException, IllegalAccessException;
	
}
