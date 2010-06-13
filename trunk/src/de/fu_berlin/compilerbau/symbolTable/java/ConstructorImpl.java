package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;

class ConstructorImpl extends MethodImpl implements Constructor {
	
	protected static final PositionString init = new PositionString("<init>", PositionBean.ZERO);
	
	public ConstructorImpl(Runtime runtime, ClassOrInterface parent,
			PositionString name, Symbol resultType,
			Iterator<Symbol> parameters, Modifier modifier) {
		super(runtime, parent, init, resultType, parameters, modifier);
	}

}
