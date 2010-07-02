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

import java.io.Serializable;

import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * Raw representation of an XML node.
 * 
 * @author rene
 */
public interface XmlNode extends StreamPosition, Serializable {
	/**
	 * type of node
	 */
	NodeType getType();
	
	/**
	 * set for: <ul>
	 *   <li>NT_TAG (tag)</li>
	 *   <li>NT_END_TAG (tag if not for <tag .../>)</li>
	 *   <li>NT_ATTR (attribute)</li>
	 *   <li>NT_PI (<a href="http://www.w3.org/TR/REC-xml/#NT-PITarget">PI target</a>)</li></ul>
	 * otherwise null or empty
	 */
	PositionString getKey();
	
	/**
	 * set for: <ul>
	 *   <li>NT_TEXT (text content)</li>
	 *   <li>NT_COMMENT (comment)</li>
	 *   <li>NT_ATTR (value)</li>
	 * set if not empty:
	 *   <li>NT_PI (processing instruction)</li></ul>
	 * otherwise null or empty
	 */
	PositionString getValue();
}
