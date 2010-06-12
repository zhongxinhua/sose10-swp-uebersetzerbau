package de.fu_berlin.compilerbau.symbolTable;

public interface QualifiedSymbol extends Symbol {
	
	String getCanonicalName();
	
	String getJavaSignature();
	
	SymbolType getType();
	
	Modifier getModifier();
	
}
