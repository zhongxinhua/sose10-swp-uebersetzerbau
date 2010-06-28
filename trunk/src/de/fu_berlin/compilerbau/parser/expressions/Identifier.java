package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Ein Identifier stellt im abstrakten Syntaxbaum einen 
 * Bezeichner dar. Es wird auch für Memberzugriffe verwendet.
 * @author Markus
 */
@SuppressWarnings("serial")
public class Identifier extends Expression {
	private PositionString name;
	
	public Identifier(PositionString name) {
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
	public PositionString getName(){
		return name;
	}
	//END get-Methoden f�r Builder

}
