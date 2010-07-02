package de.fu_berlin.compilerbau.parser.expressions;

import java.util.List;

import de.fu_berlin.compilerbau.util.PositionString;

/**
 * ObjectCreation stellt eine Konstruktoraufruf dar und ist somit eine Spezialisierung von FunctionCall.
 * @see FunctionCall
 * @author Markus
 */
@SuppressWarnings("serial")
public class ObjectCreation extends FunctionCall {

	public ObjectCreation(PositionString name, List<Expression> actualArguments) {
		super(name, actualArguments);
	}

    @Override
    public String toString() {
    	return "new("+name+")";
    }
}
