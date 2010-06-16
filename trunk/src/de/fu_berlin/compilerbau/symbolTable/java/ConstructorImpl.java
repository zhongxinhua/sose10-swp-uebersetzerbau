package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;

class ConstructorImpl extends MethodImpl implements Constructor {
	
	protected static final PositionString INIT = new PositionString("<init>", PositionBean.ZERO);
	
	public ConstructorImpl(Runtime runtime, ClassOrInterface parent,
			Iterator<Symbol> parameters, Modifier modifier) {
		super(runtime, parent, INIT, runtime.getVoid(), parameters, modifier);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CONSTRUCTOR;
	}

}
