package de.fu_berlin.compilerbau.symbolTable;

/**
 * If {@link Symbol#hasType(SymbolType)} returns true, you may safely assume
 * the {@link Symbol symbol} in question to implement the associated interface.
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
	 * @see ClassOrInterface
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
