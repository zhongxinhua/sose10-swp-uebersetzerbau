package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

/**
 * This type represents a class, if not {@link Void} or {@link PrimitiveType}.
 * @author rene
 */
public interface Class extends ClassOrInterface {
	
	Member addMember(PositionString name, Symbol type, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * @see ClassOrInterface#addMethod(PositionString, Symbol, List, Modifier)
	 */
	Constructor addConstructor(StreamPosition pos, Iterator<Variable> parameters, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	Symbol getSuperClass();
	
}
