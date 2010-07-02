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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Writes content into a ZIP file (remember: JARs are ZIPs)
 * @author rene
 */
public class ZipDirectoryWriter extends ArchiveDirectoryWriter {

	protected final ZipOutputStream zip;

	public ZipDirectoryWriter(OutputStream dest) throws IOException {
		zip = new ZipOutputStream(dest);
	}
	
	@Override
	protected void closeFile() throws IOException {
		zip.closeEntry();
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
		// outChannel does not need to be closed
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
