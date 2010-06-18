package de.fu_berlin.compilerbau.symbolTable;

import java.util.Iterator;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * @author kijewski
 */
public interface Package extends SymbolContainer, QualifiedSymbol {
	
	/**
	 * @return immutable list
	 */
	Set<ClassOrInterface> getClassesAndInterfaces();
	
	/**
	 * @return immutable list
	 */
	Set<Class> getClasses();
	
	/**
	 * @return immutable list
	 */
	Set<Interface> getInterfaces();

	Interface addInterface(PositionString name, Iterator<Symbol> extends_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;

	Class addClass(PositionString name, Symbol extends_, Iterator<Symbol> implements_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
}
