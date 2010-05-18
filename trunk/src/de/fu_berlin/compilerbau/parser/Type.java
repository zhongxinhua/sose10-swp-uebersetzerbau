package de.fu_berlin.compilerbau.parser;

public class Type {
	static final Type TYPE_STRING = new Type() {
		{
			this.name = "string";
		}
	};
	static final Type TYPE_INT = new Type() {
		{
			this.name = "int";
		}
	};
	static final Type TYPE_FLOAT = new Type() {
		{
			this.name = "float";
		}
	};
	String name;
}