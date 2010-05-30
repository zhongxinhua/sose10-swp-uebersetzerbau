package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein MemberAccess stellt im abstrakten Syntaxbaum einen 
 * Mitgliedszugriffs eines Objektes dar. Es gibt zum einen den 
 * "parent", der Eigentümer des Members und das "child" das 
 * eigentliche Member.
 * @author Markus
 */
public class MemberAccess extends Expression {	
	private Expression child;
	private Expression parent;

	public MemberAccess(Expression parent, Expression child) {
		this.parent = parent;
		this.child  = child;
	}
	
	@Override
	public Type getType() {
		//TODO muss während der semantischen Analyse ermittelt werden
		return null;
	}

	@Override
	public void printTree(int deep) {
		parent.printTree(deep+1);
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(this);
		child.printTree(deep+1);
	}
	
	@Override
	public String toString() {
		return ".";
	}
	
	public Expression getChild() {
		return child;
	}
}