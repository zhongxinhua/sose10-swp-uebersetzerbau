package de.fu_berlin.compilerbau.directoryWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

final class BufferOutputStream extends OutputStream {
	
	protected static final int BUFFER_BLOCK_LENGTH = 16384;
	protected static class SingleBuffer {
		final byte[] array = new byte[BUFFER_BLOCK_LENGTH];
		int position = 0;
		SingleBuffer next = null;
	}
	
	final UnifyingDirectoryWriter parent;
	final CharSequence fileName;
	final CharSequence folder;
	
	final SingleBuffer firstBuffer = new SingleBuffer();
	SingleBuffer lastBuffer = firstBuffer;
	int additionalBuffers = 0;
	
	BufferOutputStream(UnifyingDirectoryWriter parent, CharSequence fileName, CharSequence folder) {
		this.parent = parent;
		this.fileName = fileName;
		this.folder = folder;
	}
	
	@Override
	public void write(int b) throws IOException {
		if(lastBuffer.position >= BUFFER_BLOCK_LENGTH) {
			++additionalBuffers;
			lastBuffer = lastBuffer.next = new SingleBuffer();
		}
		lastBuffer.array[lastBuffer.position++] = (byte)b;
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		while(len > 0) {
			if(lastBuffer.position >= BUFFER_BLOCK_LENGTH) {
				++additionalBuffers;
				lastBuffer = lastBuffer.next = new SingleBuffer();
			}
			final int toWrite = Math.min(len, BUFFER_BLOCK_LENGTH-lastBuffer.position);
			System.arraycopy(b, off, lastBuffer.array, lastBuffer.position, toWrite);
			len -= toWrite;
			off += toWrite;
			lastBuffer.position += toWrite;
		}
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
	@Override
	public void close() throws IOException {
		final int contentLength = lastBuffer.position + additionalBuffers*BUFFER_BLOCK_LENGTH;
		final byte[] array = new byte[contentLength];
		int position = 0;
		for(SingleBuffer singleBuffer = firstBuffer; singleBuffer != null; singleBuffer = singleBuffer.next) {
			final int len = singleBuffer.position;
			if(len > 0) {
				System.arraycopy(singleBuffer.array, 0, array, position, len);
				position += len;
			}
		}
		try {
			parent.writeFile(array, fileName, folder);
		} catch(IllegalAccessException e) { // only possible with concurrency
			throw new RuntimeException("Concurrency exception: Another file was opened after this one!", e);
		}
	}
	
}

/**
 * Unifies {@link DirectoryWriter#openFile(CharSequence, CharSequence)} and 
 * {@link DirectoryWriter#writeFile(int, InputStream, CharSequence, CharSequence)} in
 * {@link DirectoryWriter#writeFile(byte[], CharSequence, CharSequence)}.
 * @author rene
 */
abstract class UnifyingDirectoryWriter implements DirectoryWriter {
	
	@Override
	public OutputStream openFile(final CharSequence fileName, final CharSequence folder) {
		return new BufferOutputStream(this, fileName, folder);
	}
	
	@Override
	public void writeFile(final int contentLength, final InputStream stream, CharSequence fileName,
			CharSequence folder) throws IOException, IllegalAccessException, SyncFailedException {
		final ByteBuffer buffer = ByteBuffer.allocate(contentLength);
		
		final ReadableByteChannel inChannel = Channels.newChannel(stream);
		final int read = inChannel.read(buffer);
		inChannel.close();
		
		if(read < contentLength) {
			throw new SyncFailedException("Data stream went dry early!");
		}
		writeFile(buffer.array(), fileName, folder);
	}
	
}
