package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;


public interface Method extends SymbolContainer {
	
	List<Symbol> getParameters();
	Symbol getReturnType();
	
}
