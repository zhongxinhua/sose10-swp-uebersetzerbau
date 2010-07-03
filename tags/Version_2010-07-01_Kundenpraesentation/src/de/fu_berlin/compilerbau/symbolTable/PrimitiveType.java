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

package de.fu_berlin.compilerbau.symbolTable;

/**
 * This is boolean, byte, ...
 * 
 * <p/>Bear in mind: {@link Void} inherits PrimitiveType but is not a type at all!
 * @author rene
 */
public interface PrimitiveType extends Class {

	/**
	 * E.g. returns <code>byte.class</code>
	 */
	java.lang.Class<?> getJavaClass();

	/**
	 * E.g. returns <code>Byte.class</code>
	 */
	java.lang.Class<?> getWrapperClass();
	
}
