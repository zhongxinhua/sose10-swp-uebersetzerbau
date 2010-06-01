package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;


public interface Scope extends SymbolContainer {

	Interface addInterface(PositionString name, Symbol type, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
	Variable addVariable();
	
}