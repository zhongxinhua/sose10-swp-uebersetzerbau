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

import java.io.IOException;

/**
 * As most of the archives have a similar API; it is wrapped in this class.
 * @see <a href="http://www.google.com/search?q=libarchive">Libarchive</a>
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
