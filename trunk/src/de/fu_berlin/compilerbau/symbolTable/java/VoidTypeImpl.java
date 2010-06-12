package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;

public class VoidTypeImpl extends SymbolImpl implements QualifiedSymbol {

	public VoidTypeImpl(Runtime runtime) {
		super(runtime, runtime);
	}

	@Override
	public String getCanonicalName() {
		return null;
	}

	@Override
	public String getJavaSignature() {
		return null;
	}

	@Override
	public Modifier getModifier() {
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VOID;
	}

}
