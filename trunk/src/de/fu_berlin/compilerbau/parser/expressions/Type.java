package de.fu_berlin.compilerbau.parser.expressions;

public class Type {
	public static final Type STRING  = new Type() { 
		public String toString() { return "type(string)"; } };
	public static final Type INTEGER = new Type() { 
		public String toString() { return "type(integer)"; } };
	public static final Type FLOAT   = new Type() { 
		public String toString() { return "type(float)"; } };
	public static final Type NULL    = new Type() { 
		public String toString() { return "type(null)"; } };

	public static Type get(String type) {
		if (type.toString().equals("string")) {
			return STRING;
		} else if (type.toString().equals("int")) {
			return INTEGER;
		} else if (type.toString().equals("float")) {
			return FLOAT;
		} else {
			return ReferenceType.get(type);
		}
	}
	
	public static String toJavaString(Type type) {
		if (type == STRING) {
			return "String";
		} else if (type == INTEGER) {
			return "int";
		} else if (type == FLOAT) {
			return "float";
		} else {
			return "null";
		}
	}
}