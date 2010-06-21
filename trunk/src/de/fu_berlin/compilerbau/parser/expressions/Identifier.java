package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein Identifier stellt im abstrakten Syntaxbaum einen 
 * Bezeichner dar. Es wird auch für Memberzugriffe verwendet.
 * @author Markus
 */
public class Identifier extends Expression {
	private CharSequence name;
	
	public Identifier(CharSequence name) {
		this.name = name;
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
	//BEGIN get-Methoden f�r Builder
	public CharSequence getName(){
		return name;
	}
	//END get-Methoden f�r Builder

}
