package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.symbolTable.Symbol;

public class SyntaxTreeNode {
	private Symbol symbol;
	
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public Symbol getSymbol() {
		return this.symbol;
	}
}
