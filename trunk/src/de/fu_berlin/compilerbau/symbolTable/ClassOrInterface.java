package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

public interface ClassOrInterface extends SymbolContainer {
	
	/**
	 * For interfaces you must not add a method body.
	 */
	Method addMethod(PositionString name, Symbol resultType, List<Symbol> parameters, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
}