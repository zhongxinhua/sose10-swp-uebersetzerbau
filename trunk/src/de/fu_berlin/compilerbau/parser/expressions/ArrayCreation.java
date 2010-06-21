package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

/**
 * ArrayCreation stellt im Syntaxbaum die Erzeugung eines Array mithilfe 
 * von geschweiften Klammern dar.
 * @author Markus
 */
public class ArrayCreation extends Expression {
	private List<Expression> elements;
	
	public List<Expression> getElements() { return elements; }

	public ArrayCreation(List<Expression> elements) {
		this.elements = elements;
	}

	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println("array(...)");
		deep++;
		for(Expression e: elements)
			e.printTree(deep);
	}

}
