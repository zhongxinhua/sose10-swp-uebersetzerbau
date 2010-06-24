package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Void;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;

class VoidTypeImpl extends PrimitiveTypeImpl implements Void {

	public VoidTypeImpl(Runtime runtime) throws InvalidIdentifierException {
		super(runtime, java.lang.Void.class);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VOID;
	}
	
	@Override
	public String toString() {
		return "void";
	}

}
