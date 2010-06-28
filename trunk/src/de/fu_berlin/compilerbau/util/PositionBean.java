package de.fu_berlin.compilerbau.util;

public class PositionBean implements StreamPosition {
	
	public static PositionBean ONE_ONE_ONE = new PositionBean(1,1,1);
	public static PositionBean ZERO = new PositionBean(0,0,0);
	
	private static final long serialVersionUID = 3886058529055495237L;
	
	protected int start, line, character;
	
	public PositionBean(int start, int line, int character) {
		this.start = start;
		this.line = line;
		this.character = character;
	}
	
	public PositionBean(StreamPosition pos) {
		this.start = pos.getStart();
		this.line = pos.getLine();
		this.character = pos.getColumn();
	}
	
	@Override
	public int getColumn() {
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
	
	@Override
	public String toString() {
		return "[" + line + ":" + character + "=@" + start + "]";
	}
	
}
