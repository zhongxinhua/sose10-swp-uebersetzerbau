package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.util.PositionString;

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
	public static Type get(PositionString attributeValue) {
		// TODO Auto-generated method stub
		return null;
	}
}