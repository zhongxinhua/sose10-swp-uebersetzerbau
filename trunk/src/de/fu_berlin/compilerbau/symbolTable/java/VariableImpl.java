package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class VariableImpl extends SymbolImpl implements Variable, Comparable<Variable> {
	
	protected final PositionString name;
	protected final Modifier modifier;

	public VariableImpl(Runtime runtime, SymbolContainer parent, PositionString name, Modifier modifier) {
		super(runtime, parent);
		this.name = name;
		this.modifier = modifier;
	}

	@Override
	public String getName() {
		return name.toString();
	}

	@Override
	public String getJavaSignature() {
		// TODO Auto-generated method stub
		return null;
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
	public int compareTo(Variable right) {
		return name.compareTo(right.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
