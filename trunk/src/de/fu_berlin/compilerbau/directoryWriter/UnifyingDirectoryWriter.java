package de.fu_berlin.compilerbau.directoryWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;

/**
 * Unifies {@link DirectoryWriter#openFile(CharSequence, CharSequence)} and 
 * {@link DirectoryWriter#writeFile(int, InputStream, CharSequence, CharSequence)} in
 * {@link DirectoryWriter#writeFile(byte[], CharSequence, CharSequence)}.
 * @author rene
 */
abstract class UnifyingDirectoryWriter implements DirectoryWriter {
	
	protected static final int BUFFER_BLOCK_LENGTH = 16384;
	protected static class SingleBuffer {
		final byte[] buffer = new byte[BUFFER_BLOCK_LENGTH];
		int position = 0;
		SingleBuffer next = null;
	}
	
	@Override
	public OutputStream openFile(final CharSequence fileName, final CharSequence folder)
			throws IOException, IllegalAccessException {
		return new OutputStream() {
			final SingleBuffer firstBuffer = new SingleBuffer();
			SingleBuffer lastBuffer = firstBuffer;
			int additionalBuffers = 0;
			
			@Override
			public void write(int b) throws IOException {
				if(lastBuffer.position >= BUFFER_BLOCK_LENGTH) {
					++additionalBuffers;
					lastBuffer = lastBuffer.next = new SingleBuffer();
				}
				lastBuffer.buffer[lastBuffer.position] = (byte)b;
			}
			
			@Override
			public void close() throws IOException {
				final int contentLength = lastBuffer.position + additionalBuffers*BUFFER_BLOCK_LENGTH;
				final byte[] array = new byte[contentLength];
				for(SingleBuffer buffer = firstBuffer; buffer != null; buffer = buffer.next) {
					System.arraycopy(buffer, 0, array, 0, buffer.position);
				}
				try {
					writeFile(array, fileName, folder);
				} catch(IllegalAccessException e) {
					throw new RuntimeException("Another file was opened after " + this, e);
				}
			}
			
		};
	}
	
	@Override
	public void writeFile(final int contentLength, final InputStream stream, CharSequence fileName,
			CharSequence folder) throws IOException, IllegalAccessException, SyncFailedException {
		final byte[] array = new byte[contentLength];
		int offset = 0;
		while(offset < contentLength) {
			int read = stream.read(array, offset, contentLength-offset);
			if(read < 0) {
				throw new SyncFailedException("Data stream went dry too early!");
			}
		}
		writeFile(array, fileName, folder);
	}
	
}
