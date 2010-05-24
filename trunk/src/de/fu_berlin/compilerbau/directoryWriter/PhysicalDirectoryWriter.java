package de.fu_berlin.compilerbau.directoryWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class PhysicalDirectoryWriter implements DirectoryWriter {
	
	private final File root;

	/**
	 * Ensures existence of root.
	 * @param root
	 */
	public PhysicalDirectoryWriter(File root) {
		this.root = root;
		root.mkdirs();
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void close() throws IOException {
		// ignore
	}

	/**
	 * There may be more than one file open at a time.
	 * @see DirectoryWriter#openFile(CharSequence, CharSequence)
	 */
	@Override
	public OutputStream openFile(CharSequence fileName, CharSequence folder) throws IOException {
		final File path = new File(root, folder.toString());
		path.mkdirs();
		final File dest = new File(path, fileName.toString());
		return new FileOutputStream(dest);
	}

	@Override
	public void writeFile(final int contentLength, final InputStream stream, CharSequence fileName,
			CharSequence folder) throws IOException, SyncFailedException {
		final File path = new File(root, folder.toString());
		path.mkdirs();
		final File dest = new File(path, fileName.toString());
		
		final FileOutputStream outputStream = new FileOutputStream(dest);
		final FileChannel outChannel = outputStream.getChannel();
		final ReadableByteChannel inChannel = Channels.newChannel(stream);
		long read = outChannel.transferFrom(inChannel, 0, contentLength);
		if(read < contentLength) {
			throw new SyncFailedException("Data stream went dry early!");
		}
	}
	
	@Override
	public void writeFile(byte[] input, CharSequence fileName, CharSequence folder)
			throws IOException {
		final File path = new File(root, folder.toString());
		path.mkdirs();
		final File dest = new File(path, fileName.toString());
		
		final FileOutputStream outputStream = new FileOutputStream(dest);
		outputStream.getChannel().write(ByteBuffer.wrap(input));
		outputStream.close();
		
	}
	
}