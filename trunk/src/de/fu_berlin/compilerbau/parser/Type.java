package de.fu_berlin.compilerbau.parser;

public class Type {
	String name;

	static final public Type TYPE_STRING = new Type() {
		{
			this.name = "string";
		}
	};
	static final public Type TYPE_INT = new Type() {
		{
			this.name = "int";
		}
	};
	static final public Type TYPE_FLOAT = new Type() {
		{
			this.name = "float";
		}
	};
	static final public Type TYPE_REF = new Type() {
		{
			this.name = "ref";
		}
	};

	public static Type get(String type) {
		if (type.toString().equals("string")) {
			return TYPE_STRING;
		} else if (type.toString().equals("int")) {
			return TYPE_INT;
		} else if (type.toString().equals("float")) {
			return TYPE_FLOAT;
		} else if (type.toString().equals("ref")){
			return TYPE_REF;
		}else {
			//Prüfung auf null erforderlich 
			return null;
		}
	}
}