package de.fu_berlin.compilerbau.symbolTable;


public interface UnqualifiedSymbol extends Symbol {
	
	enum Likelyness {
		IMPOSSIBLE,
		MAYBE,
		YES
	}
	
	/**
	 * Does the symbol belong to a type? (Replicates {@link #isConstructor()})
	 */
	Likelyness isType();
	
	/**
	 * Does the symbol belong to a constructor? (Implies {@link #isType()})
	 */
	Likelyness isConstructor();
	
	/**
	 * Does the symbol belong to a package?
	 */
	Likelyness isPackage();
	
	
}
