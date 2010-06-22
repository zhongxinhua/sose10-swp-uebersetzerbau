package de.fu_berlin.compilerbau.symbolTable;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
	METHOD;

	private static final Boolean[][] IMPLICATIONS = {
		/*                       UNQUALIFIED, CLASS,  CLASS_OR_INTERFACE, CONSTRUCTOR, INTERFACE, MEMBER, PACKAGE, RUNTIME, VARIABLE, SCOPE, PRIMITIVE_TYPE, ARRAY_TYPE, VOID,  METHOD*/
		/*UNQUALIFIED*/        { null,        null,   null,               null,        null,      null,   null,    null,    null,     null,  null,           null,       null,  null  },
		/*CLASS*/              { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, FALSE },
		/*CLASS_OR_INTERFACE*/ { null,        FALSE,  TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*CONSTRUCTOR*/        { null,        FALSE,  FALSE,              TRUE,        FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, TRUE  },
		/*INTERFACE*/          { null,        FALSE,  TRUE,               FALSE,       TRUE,      FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*MEMBER*/             { null,        FALSE,  FALSE,              FALSE,       FALSE,     TRUE,   FALSE,   FALSE,   TRUE,     FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*PACKAGE*/            { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  TRUE,    FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*RUNTIME*/            { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   TRUE,    FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*VARIABLE*/           { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   TRUE,     FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*SCOPE*/              { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, FALSE },
		/*PRIMITIVE_TYPE*/     { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, TRUE,           FALSE,      FALSE, FALSE },
		/*ARRAY_TYPE*/         { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          TRUE,       FALSE, FALSE },
		/*VOID*/               { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, TRUE,           FALSE,      TRUE,  FALSE },
		/*METHOD*/             { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, TRUE  }
	};
	static {
		if(IMPLICATIONS.length != SymbolType.values().length) {
			throw new RuntimeException("IMPLICATION.length != SymbolType.values().length");
		}
		for(Boolean[] IMPLICATIONS_LINE : IMPLICATIONS) {
			if(IMPLICATIONS_LINE.length != SymbolType.values().length) {
				throw new RuntimeException("IMPLICATIONS_LINE.length != SymbolType.values().length");
			}
		}
	}
	
	public Boolean implicates(SymbolType rightType) {
		final Boolean result = IMPLICATIONS[ordinal()][rightType.ordinal()];
		return result;
	}
	
	public static Boolean implicates(SymbolType leftType, SymbolType rightType) {
		final Boolean result = IMPLICATIONS[leftType.ordinal()][rightType.ordinal()];
		return result;
	}
	
}
