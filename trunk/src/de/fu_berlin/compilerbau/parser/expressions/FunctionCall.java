package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

import de.fu_berlin.compilerbau.parser.Function;

public class FunctionCall extends Expression { // a(b,c)
	Function function;
	List<Expression> actualParams;
	
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printTree(int deep) {
		// TODO Auto-generated method stub
		
	}
}