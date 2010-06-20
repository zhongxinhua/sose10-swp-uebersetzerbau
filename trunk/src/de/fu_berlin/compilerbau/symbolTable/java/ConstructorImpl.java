package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ConstructorImpl extends MethodImpl implements Constructor {
	
	protected static final String INIT = "<init>";
	
	public ConstructorImpl(Runtime runtime, ClassOrInterface parent, StreamPosition pos,
			Iterator<Variable> parameters, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent, new PositionString(INIT, pos), runtime.getVoid(), parameters, modifier);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CONSTRUCTOR;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(parent.getDestinationName());
		builder.append('.');
		builder.append(destionationName);
		builder.append('(');
		if(!parameters.isEmpty()) {
			Iterator<Variable> i = parameters.iterator();
			builder.append(i.next());
			while(i.hasNext()) {
				builder.append(", ");
				builder.append(i.next());
			}
		}
		builder.append(')');
		return builder.toString();
	}

}
