package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

/**
 * The Runtime contains all packages that will be available to
 * the {@link Package packages}. It contains all packages.
 * @author kijewski
 */
public interface Runtime extends SymbolContainer {
	
	/**
	 * @return (immutable) list
	 */
	List<Package> getPackages();
	
	/**
	 * If set, an Exception is thrown when a symbol gets shadowed.
	 */
	boolean getThrowsAtShadowing();
	
	/**
	 * @see #getThrowsAtShadowing()
	 */
	void setThrowsAtShadowing();
	
	/**
	 * Adds a new package.
	 * @param name The canonical name of the package.
	 * @param isStaticImport "Adopt" every visible symbol to this scope.
	 * @return a {@link Package} to fill top-down.
	 */
	Package addPackage(PositionString name, Modifier modifier) throws
			DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException;
	
}
