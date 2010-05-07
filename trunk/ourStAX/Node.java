package ourStAX;

class Node extends Position implements INode {
	
	private final NodeType type;
	private final String key, value;
	
	Node(int start, int line, int character, NodeType type, String key, String value) {
		super(start, line, character);
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	public NodeType getType() {
		return type;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[" + type + "|" + key + "=" + value + "]@" + super.toString();
	}
	
}
