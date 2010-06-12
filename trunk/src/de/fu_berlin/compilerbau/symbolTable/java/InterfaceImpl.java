package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;

public class InterfaceImpl extends ClassOrInterfaceImpl implements Interface {

	public InterfaceImpl(Runtime runtime, SymbolContainer parent, Iterator<Symbol> implements_,
			Modifier modifier, String canonicalName) {
		super(runtime, parent, implements_, modifier, canonicalName);
	}

}
