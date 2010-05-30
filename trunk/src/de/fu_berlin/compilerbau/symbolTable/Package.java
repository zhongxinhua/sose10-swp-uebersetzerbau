package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * @author kijewski
 */
public interface Package extends SymbolContainer {
	
	Runtime getRuntime();
	
	/**
	 * @return immutable list
	 */
	List<ClassOrInterface> getClassesAndInterfaces();
	
	/**
	 * @return immutable list
	 */
	List<Class> getClasses();
	
	/**
	 * @return immutable list
	 */
	List<Interface> getInterfaces();

	Interface addInterface(PositionString name, List<Symbol> extends_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;

	Class addClass(PositionString name, Symbol extends_, List<Symbol> implements_, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
}
