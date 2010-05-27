package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

/**
 * Ein FunctionCall stellt im abstrakten Syntaxbaum einen 
 * einen Funktionsaufruf dar. Es enthält den Namen der Funktion 
 * und alle konkreten Parameter in Form von Ausdrücken.
 * @author Markus
 */
public class FunctionCall extends Expression { // a(b,c)
	protected CharSequence name;
	protected List<Expression> actualArguments;
	
	public FunctionCall(CharSequence name, List<Expression> actualArguments) {
		this.name = name;
		this.actualArguments = actualArguments;
	}
	
	@Override
	public Type getType() {
		//TODO: während der smeantischen Analyse den Typ noch rausfinden
		return null;
	}

	@Override
	public void printTree(int deep) {
		for(int d=deep; d>0; d--) System.out.print("  ");
		System.out.println(this);
		++deep;
		for(Expression arg: actualArguments)
			arg.printTree(deep);
	}
	
	@Override
	public String toString() {
		return "functionCall("+name+")";
	}
}