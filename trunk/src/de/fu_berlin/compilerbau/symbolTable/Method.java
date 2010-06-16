package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;


public interface Method extends Scope, QualifiedSymbol {
	
	List<Symbol> getParameters();
	Symbol getReturnType();
	Scope getScope();
	
}
