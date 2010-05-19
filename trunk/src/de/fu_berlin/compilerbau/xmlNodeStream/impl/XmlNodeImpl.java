package de.fu_berlin.compilerbau.xmlNodeStream.impl;

import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;

/**
 * A found node.
 * @author rene
 */
class XmlNodeImpl implements XmlNode {
	
	private static final long serialVersionUID = -7485085468103238711L;
	
	private final NodeType type;
	private final PositionString key, value;
	private final int start, line, character;
	
	public XmlNodeImpl(int start, int line, int character, NodeType type, PositionString key, PositionString value) {
		this.start = start;
		this.line = line;
		this.character = character;
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

	@Override
	public int getCharacter() {
		return character;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getStart() {
		return start;
	}
	
}
