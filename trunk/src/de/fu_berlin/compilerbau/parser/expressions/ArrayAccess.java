package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

/**
 * ArrayAccess stellt im abstrakten Syntaxbaum den Array-Zugriff da.
 * Die Klasse enthÃ¤lt Informationen Ã¼ber Namen und Indizies des 
 * Arrayzugriffes.
 * @author Markus
 */
public class ArrayAccess extends Expression {
	private CharSequence name;
	private List<Expression> indices;
	
	public ArrayAccess(CharSequence name, List<Expression> indices) {
		this.name = name;
		this.indices = indices;
	}
	
	@Override
	public Type getType() {
		// TODO: muss Ã¼ber die semantische Analyse noch festgestellt werden
		return null;
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
	//BEGIN get-Methoden für Builder
	public CharSequence getName(){
		return name;
	}
	public List<Expression> getIndices(){
		return indices;
	}
	//END get-Methoden für Builder
}
