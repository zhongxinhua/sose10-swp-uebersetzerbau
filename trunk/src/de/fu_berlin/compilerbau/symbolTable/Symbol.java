package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.StreamPosition;

public interface Symbol {
	
	String getCanonicalName();
	
	String getJavaSignature();
	
	/**
	 * Returns the position where the symbol was declared, i.e. an interface.
	 * @return null if position is unknown
	 */
	StreamPosition getDeclarationPosition();
	
	/**
	 * Returns the position where the symbol was implemented.
	 * @return null if position is unknown
	 */
	StreamPosition getImplementationPosition();
	
	/**
	 * @return immutable list [(who,where)]
	 */
	List<Pair<Symbol,StreamPosition>> getMentions();
	
}
