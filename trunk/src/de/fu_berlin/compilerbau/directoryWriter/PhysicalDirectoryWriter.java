/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

/**
 * "Physical" hereby means an actual directory in the file system.
 * @author rene
 */
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
		final File dest = new File(path, fileName.toString() + ".java");
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
