package de.fu_berlin.compilerbau.parser.expressions;

public class Identifier extends Expression {
	private CharSequence name;
	private Type type;
	
	public Identifier(CharSequence name) {
		this.name = name;
	}

	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "identifier("+name+")";
	}

	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(this);
	}

}
