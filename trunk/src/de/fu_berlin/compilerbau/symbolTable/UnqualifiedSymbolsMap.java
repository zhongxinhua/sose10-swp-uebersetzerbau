package de.fu_berlin.compilerbau.symbolTable;

import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;

public interface UnqualifiedSymbolsMap<U extends UnqualifiedSymbol> {
	
	public static enum ReplaceFunResult {
		REPLACED,
		NOT_REPLACED
	}

	public static interface ReplaceFunc {
		
		/**
		 * @return if the symbol could be replaced
		 */
		public ReplaceFunResult replace() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
		
	}
	
	public void addUnqualifiedSymbol(U unqualifiedSymbol, ReplaceFunc replacer);
	
	/**
	 * @see Runtime#qualifyAllSymbols()
	 */
	public Set<UnqualifiedSymbol> qualifyAllSymbols() throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException;
	
	boolean hasUnqualifiedSymbols();

}
