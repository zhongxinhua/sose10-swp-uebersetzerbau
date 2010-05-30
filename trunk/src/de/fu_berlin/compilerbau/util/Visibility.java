package de.fu_berlin.compilerbau.util;


public enum Visibility {
	
	PRIVATE("private"),
	PROTECTED("protected"),
	DEFAULT(""),
	PUBLIC("public");
	
	public final String name;

	private Visibility(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
