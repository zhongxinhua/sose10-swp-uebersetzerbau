package de.fu_berlin.compilerbau.symbolTable;

import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.StreamPosition;

public interface Symbol {
	
	Runtime getRuntime();
	
	String getCanonicalName();
	
	String getJavaSignature();
	
	SymbolType getType();
	
	/**
	 * Returns the position where the symbol was implemented.
	 * @return null if position is unknown
	 */
	StreamPosition getPosition();
	
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
	
}
