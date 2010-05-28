package de.fu_berlin.compilerbau.symbolTable;

import java.util.Collection;

public interface Symbol {
	
	/**
	 * Every non-abstract Symbol has a parent scope.
	 * @return parent {@link SymbolTable} or null
	 */
	SymbolTable getParent();
	
	/**
	 * Some Symbols 
	 * @return null or this very element {@code Symbol}
	 */
	SymbolTable isSymbolTable();
	
	/**
	 * Every non-abstract Symbol has a signature.
	 * @return signature or null
	 */
	String getSignature();
	
	/**
	 * Every element has a name.
	 */
	String getName();
	
	/**
	 * Is there evidence that this Symbol belongs to a ctor?
	 */
	TernaryBool isConstructor();
	
	/**
	 * Is there evidence that this Symbol belongs to a typename?
	 */
	TernaryBool isTypename();

	/**
	 * Is there evidence that this Symbol belongs to a variable?
	 */
	TernaryBool isVariable();
	
	/**
	 * A list of the Symbols this very Symbol shadows.
	 */
	Collection<Symbol> getShadowedSymbols();
	
}
