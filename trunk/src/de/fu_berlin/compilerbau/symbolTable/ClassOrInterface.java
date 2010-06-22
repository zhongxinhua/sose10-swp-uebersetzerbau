package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * Base type of {@link Class classes}, {@link Interface interfaces},
 * {@link PrimitiveType primitive types} and {@link Void void types}.
 * @author rene
 */
public interface ClassOrInterface extends SymbolContainer, QualifiedSymbol, HasComparator<ClassOrInterface> {
	
	/**
	 * For interfaces you must not add a method body.
	 */
	Method addMethod(PositionString name, Symbol resultType, Iterator<Variable> parameters,
			Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	/**
	 * If this if an {@link Interface} itself, read getExtendedInterfaces ...
	 */
	Set<Symbol> getImplementedInterfaces();
	
}
