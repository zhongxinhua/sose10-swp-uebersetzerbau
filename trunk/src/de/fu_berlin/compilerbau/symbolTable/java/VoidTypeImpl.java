package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Void;

public class VoidTypeImpl extends PrimitiveTypeImpl implements Void {

	public VoidTypeImpl(Runtime runtime) {
		super(runtime, java.lang.Void.class);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VOID;
	}

}
