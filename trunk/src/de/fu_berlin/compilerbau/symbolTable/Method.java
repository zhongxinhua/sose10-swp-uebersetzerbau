package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;


public interface Method extends SymbolContainer, QualifiedSymbol {
	
	List<Symbol> getParameters();
	Symbol getReturnType();
	
}
