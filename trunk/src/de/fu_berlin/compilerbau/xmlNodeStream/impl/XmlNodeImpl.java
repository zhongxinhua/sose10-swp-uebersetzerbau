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
