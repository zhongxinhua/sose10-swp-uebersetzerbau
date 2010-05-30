package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

public interface Class extends ClassOrInterface {
	
	Member addMember(PositionString name, Symbol type, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
	Constructor addConstructor(List<Symbol> parameters, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
}
