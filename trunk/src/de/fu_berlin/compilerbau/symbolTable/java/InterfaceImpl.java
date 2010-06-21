package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class InterfaceImpl extends ClassOrInterfaceImpl implements Interface {
	
	protected final StreamPosition position;

	public InterfaceImpl(Runtime runtime, Package parent, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) throws InvalidIdentifierException {
		super(runtime, parent, implements_, modifier, canonicalName);
		this.position = canonicalName;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.INTERFACE;
	}
	
	@Override
	public String toString() {
		return ((Package)getParent()).getDestinationName() + "." + destinationName;
	}

}
