package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
	private final Runtime runtime;
	private final SymbolContainer parent;
	protected final Set<Map.Entry<Symbol, StreamPosition>> mentions = new TreeSet<Map.Entry<Symbol, StreamPosition>>();
	
	SymbolImpl(Runtime runtime, SymbolContainer parent) {
		this.runtime = runtime;
		this.parent = parent ;
	}
	
	@Override
	public void addMention(Symbol who, StreamPosition where) {
		Mention mention = new Mention(who, where);
		mentions.add(mention);
	}
	
	@Override
	public Set<Map.Entry<Symbol, StreamPosition>> getMentions() {
		return mentions;
	}
	
	@Override
	public SymbolContainer getParent() {
		return parent;
	}

	@Override
	public Runtime getRuntime() {
		return runtime;
	}
	
	private static final Boolean[][] IMPLICATIONS = {
		/*                       UNQUALIFIED, CLASS,  CLASS_OR_INTERFACE, CONSTRUCTOR, INTERFACE, MEMBER, PACKAGE, RUNTIME, VARIABLE, SCOPE, PRIMITIVE_TYPE, ARRAY_TYPE, VOID,  METHOD*/
		/*UNQUALIFIED*/        { null,        null,   null,               null,        null,      null,   null,    null,    null,     null,  null,           null,       null,  null },
		/*CLASS*/              { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, FALSE },
		/*CLASS_OR_INTERFACE*/ { null,        FALSE,  TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*CONSTRUCTOR*/        { null,        FALSE,  FALSE,              TRUE,        FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, TRUE },
		/*INTERFACE*/          { null,        FALSE,  TRUE,               FALSE,       TRUE,      FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*MEMBER*/             { null,        FALSE,  FALSE,              FALSE,       FALSE,     TRUE,   FALSE,   FALSE,   TRUE,     FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*PACKAGE*/            { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  TRUE,    FALSE,   FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*RUNTIME*/            { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   TRUE,    FALSE,    FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*VARIABLE*/           { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   TRUE,     FALSE, FALSE,          FALSE,      FALSE, FALSE },
		/*SCOPE*/              { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, FALSE },
		/*PRIMITIVE_TYPE*/     { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, TRUE,           FALSE,      FALSE, FALSE },
		/*ARRAY_TYPE*/         { null,        TRUE,   TRUE,               FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          TRUE,       FALSE, FALSE },
		/*VOID*/               { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    FALSE, FALSE,          FALSE,      TRUE,  FALSE },
		/*METHOD*/             { null,        FALSE,  FALSE,              FALSE,       FALSE,     FALSE,  FALSE,   FALSE,   FALSE,    TRUE,  FALSE,          FALSE,      FALSE, TRUE }
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
	
	@Override
	public Boolean hasType(SymbolType leftType) {
		if(!(this instanceof QualifiedSymbol)) {
			return null;
		}
		final SymbolType rightType = ((QualifiedSymbol)this).getType();
		final Boolean result = IMPLICATIONS[leftType.ordinal()][rightType.ordinal()];
		return result;
	}
	
}
