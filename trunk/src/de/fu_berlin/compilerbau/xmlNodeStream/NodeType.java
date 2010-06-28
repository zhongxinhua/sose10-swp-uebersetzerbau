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

package de.fu_berlin.compilerbau.xmlNodeStream;

/**
 * Content type of the {@link XmlNode}.
 * 
 * @author rene
 */
public enum NodeType {
	/**
	 * an error occurred (wrong data or broken input)
	 */
	NT_ERROR,
	
	/**
	 * text node / <a href="http://www.w3.org/TR/REC-xml/#sec-cdata-sect">CDATA section</a>
	 */
	NT_TEXT,
	
	/**
	 * <a href="http://www.w3.org/TR/REC-xml/#sec-comments">comment node</a>
	 */
	NT_COMMENT,
	
	/**
	 * an XML tag
	 */
	NT_TAG,
	
	/**
	 * end of an XML tag
	 */
	NT_END_TAG,
	
	/**
	 * <a href="http://www.w3.org/TR/REC-xml/#attdecls">attribute node</a>
	 */
	NT_ATTR,
	
	/**
	 * <a href="http://www.w3.org/TR/REC-xml/#sec-pi">processing instruction</a>
	 */
	NT_PI
}
