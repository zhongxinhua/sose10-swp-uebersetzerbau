package de.fu_berlin.compilerbau.symbolTable;

/**
 * @author kijewski
 */
public enum SymbolType {
	
	/**
	 * The type of this symbol is still unknown. When the symbol tree is fully
	 * populated, there should not be any unqualified symbols left!
	 */
	UNQUALIFIED,

	CLASS,
	
	CONSTRUCTOR,
	
	INTERFACE,
	
	MEMBER,
	
	PACKAGE,
	
	RUNTIME,
	
	VARIABLE
	
}
