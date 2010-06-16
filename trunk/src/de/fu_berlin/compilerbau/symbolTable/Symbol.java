package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.util.StreamPosition;

public interface Symbol {
	
	Runtime getRuntime();
	
	/**
	 * Where and by whom was this symbol mentioned. (For circular dependencies.)
	 * @return immutable list [(who,where)]
	 */
	Set<Map.Entry<Symbol,StreamPosition>> getMentions();
	
	void addMention(Symbol who,StreamPosition where);
	
	/**
	 * Immediately parent of this symbol.
	 * @return null if this is an {@link UnqualifiedSymbol unqualified symbol} or
	 * 	the {@link Runtime}.
	 */
	SymbolContainer getParent();
	
	/**
	 * return <code>leftType implicates (({@link QualifiedSymbol})this).getType())</code>
	 * 
	 * <p/>E.g. {@link SymbolType.CONSTRUCTOR} → {@link SymbolType.METHOD} → {@link SymbolType.SCOPE}
	 * @return null if <code>!(this instanceof {@link QualifiedSymbol})</code> 
	 */
	Boolean hasType(SymbolType leftType);
	
}
