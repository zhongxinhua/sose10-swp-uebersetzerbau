package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class UndefinedScope extends PackageImpl {

	public UndefinedScope(Runtime runtime, PositionString name)
			throws InvalidIdentifierException {
		super(runtime, name);
	}
	
	public UndefinedScope(Runtime runtime) throws InvalidIdentifierException {
		this(runtime, null);
	}

	@Override
	public String toString() {
		return "<undefined scope>";
	}
	

}
