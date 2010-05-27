package de.fu_berlin.compilerbau.parser.expressions;

public class NullLiteral extends Literal {
	public static final NullLiteral NULL = new NullLiteral();
	
	@Override
	public Type getType() {
		return Type.NULL;
	}
	
	@Override
	public String toString() {
		return "null";
	}
}
