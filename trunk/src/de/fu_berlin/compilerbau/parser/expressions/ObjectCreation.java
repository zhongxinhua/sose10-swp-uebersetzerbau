package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

public class ObjectCreation extends FunctionCall {

	public ObjectCreation(CharSequence name, List<Expression> actualArguments) {
		super(name, actualArguments);
	}


}
