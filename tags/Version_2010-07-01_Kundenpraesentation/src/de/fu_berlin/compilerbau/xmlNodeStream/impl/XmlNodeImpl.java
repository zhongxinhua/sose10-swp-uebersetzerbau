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

package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;

/**
 * A found node.
 * @author kijewski
 */
class XmlNodeImpl extends PositionBean implements XmlNode {
	
	private static final long serialVersionUID = -7485085468103238711L;
	
	private final NodeType type;
	private final PositionString key, value;
	
	public XmlNodeImpl(int start, int line, int character, NodeType type, PositionString key, PositionString value) {
		super(start, line, character);
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	public XmlNodeImpl(StreamPosition pos, NodeType type, PositionString key, PositionString value) {
		super(pos);
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public NodeType getType() {
		return type;
	}
	
	@Override
	public PositionString getKey() {
		return key;
	}
	
	@Override
	public PositionString getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[" + type + "|" + key + "=" + value + "]@" + super.toString();
	}
	
}
