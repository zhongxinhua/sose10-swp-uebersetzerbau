package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Ein FunctionCall stellt im abstrakten Syntaxbaum einen 
 * einen Funktionsaufruf dar. Es enthält den Namen der Funktion 
 * und alle konkreten Parameter in Form von Ausdrücken.
 * @author Markus
 */
@SuppressWarnings("serial")
public class FunctionCall extends Expression { // a(b,c)
	protected PositionString name;
	protected List<Expression> actualArguments;
	
	public FunctionCall(PositionString name, List<Expression> actualArguments) {
		this.name = name;
		this.actualArguments = actualArguments;
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
		return "functionCall "+name+"(...)";
	}
	//BEGIN get-Methoden f�r Builder
	public PositionString getName(){
		return name;
	}
	public List<Expression> getArguments(){
		return actualArguments;
	}
	//END get-Methoden f�r Builder
}