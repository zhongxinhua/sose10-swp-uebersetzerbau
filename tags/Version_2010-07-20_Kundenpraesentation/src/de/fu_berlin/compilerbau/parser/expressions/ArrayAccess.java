package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * ArrayAccess stellt im abstrakten Syntaxbaum den Array-Zugriff da.
 * Die Klasse enthält Informationen über Namen und Indizies des 
 * Arrayzugriffes.
 * @author Markus
 */
@SuppressWarnings("serial")
public class ArrayAccess extends Expression {
	private PositionString name;
	private List<Expression> indices;
	
	public ArrayAccess(PositionString name, List<Expression> indices) {
		this.name = name;
		this.indices = indices;
	}

	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(this);
		++deep;
		for(Expression index: indices)
			index.printTree(deep);
	}

	@Override
	public String toString() {
		return name+"[...]";
	}
	//BEGIN get-Methoden f�r Builder
	public PositionString getName(){
		return name;
	}
	public List<Expression> getIndices(){
		return indices;
	}
	//END get-Methoden f�r Builder
}
