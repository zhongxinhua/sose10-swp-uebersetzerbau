package de.fu_berlin.compilerbau.ourStAX;

class Node extends Position implements INode {
	
	private final NodeType type;
	private final String key, value;
	
	public Node(int start, int line, int character, NodeType type, String key, String value) {
		super(start, line, character);
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	@Override
	public NodeType getType() {
		return type;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[" + type + "|" + key + "=" + value + "]@" + super.toString();
	}
	
}
