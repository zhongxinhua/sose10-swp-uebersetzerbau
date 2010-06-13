package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.util.StreamPosition;

class VariableImpl extends SymbolImpl implements Variable, Comparable<Variable> {

	public VariableImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCanonicalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJavaSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Modifier getModifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamPosition getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Variable arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
