package de.fu_berlin.compilerbau.symbolTable;

/**
 * @author kijewski
 */
public enum SymbolType {
	
	/**
	 * The type of this symbol is still unknown. When the symbol tree is fully
	 * populated, there should not be any unqualified symbols left!
	 * @see UnqualifiedSymbol
	 */
	UNQUALIFIED,

	/**
	 * @see Class
	 */
	CLASS,

	/**
	 * Either {@link Class} or {@link Interface}. Cannot be the type of a qualified symbol.
	 * @see Class
	 * @see Interface
	 */
	CLASS_OR_INTERFACE,

	/**
	 * @see Constructor
	 */
	CONSTRUCTOR,

	/**
	 * @see Interface
	 */
	INTERFACE,

	/**
	 * @see Member
	 */
	MEMBER,

	/**
	 * @see Package
	 */
	PACKAGE,

	/**
	 * @see Runtime
	 */
	RUNTIME,

	/**
	 * @see Variable
	 */
	VARIABLE,

	/**
	 * @see Scope
	 */
	SCOPE,
	
	/**
	 * @see PrimitiveType
	 */
	PRIMITIVE_TYPE,
	
	/**
	 * @see ArrayType
	 */
	ARRAY_TYPE,
	
	/**
	 * @see Void
	 */
	VOID,
	
	/**
	 * @see Method
	 */
	METHOD
	
}
