package de.fu_berlin.compilerbau.ourStAX;

class Position implements IPosition {
	
	protected int start, line, character;
	
	Position(int start, int line, int character) {
		this.start = start;
		this.line = line;
		this.character = character;
	}
	
	@Override
	public int getStart() {
		return start;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	@Override
	public int getCharacter() {
		return character;
	}
	
	@Override
	public String toString() {
		return "[" + line + ":" + character + "=@" + start + "]";
	}
	
}
