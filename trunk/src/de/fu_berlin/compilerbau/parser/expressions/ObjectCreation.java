package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

/**
 * ObjectCreation stellt eine Konstruktoraufruf dar und ist somit eine Spezialisierung von FunctionCall.
 * @see FunctionCall
 * @author Markus
 */
public class ObjectCreation extends FunctionCall {

	public ObjectCreation(CharSequence name, List<Expression> actualArguments) {
		super(name, actualArguments);
	}

    @Override
    public String toString() {
    	return "new("+name+")";
    }
}
