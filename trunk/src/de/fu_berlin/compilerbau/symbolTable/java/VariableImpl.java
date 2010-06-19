package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class VariableImpl extends SymbolImpl implements Variable {
	
	protected final PositionString name;
	protected final Modifier modifier;
	protected final String destinationName;
	protected final Symbol variableType;

	public VariableImpl(Runtime runtime, SymbolContainer parent, PositionString name, Symbol variableType, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent);
		this.name = name;
		if(name != null) {
			this.destinationName = runtime.mangleName(name.toString());
			if(destinationName == null || !runtime.isValidIdentifier(destinationName)) {
				throw new InvalidIdentifierException(parent, name);
			}
		} else {
			this.destinationName = null;
		}
		this.modifier = modifier;
		this.variableType = variableType;
	}

	@Override
	public String getName() {
		return name.toString();
	}

	@Override
	public Modifier getModifier() {
		return modifier;
	}

	@Override
	public StreamPosition getPosition() {
		return name;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VARIABLE;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}

	@Override
	public int compareTo(Variable right) {
		return destinationName.compareTo(right.getDestinationName());
	}
	
	@Override
	public Symbol getVariableType() {
		return variableType;
	}

}
