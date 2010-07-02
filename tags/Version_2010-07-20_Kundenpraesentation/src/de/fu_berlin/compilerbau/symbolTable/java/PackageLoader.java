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

package de.fu_berlin.compilerbau.symbolTable.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Basically it unhides super methods. :)
 * @author kijewski
 */
class PackageLoader extends URLClassLoader {
	
	protected static URL[] concatUrls(URL rtJar, URL[] classpath) {
		final URL[] result = new URL[classpath.length + 1];
		result[0] = rtJar;
		System.arraycopy(classpath, 0, result, 1, classpath.length);
		return result;
	}
	
	public PackageLoader(URL rtJar, URL[] classpath) throws IOException {
		super(concatUrls(rtJar, classpath));
	}
	
	@Override
	public Package[] getPackages() {
		return super.getPackages();
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}
}
