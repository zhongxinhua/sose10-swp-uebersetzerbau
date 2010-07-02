package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.util.StreamPosition;

@SuppressWarnings("serial")
public class SyntaxTreeNode implements
		StreamPosition {

	private int column = 0, line = 0, start = 0;

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getLine() {
		return line;
	}
	
	public void setPosition(int line, int column, int start) {
		this.line = line;
		this.column = column;
		this.start = start;
	}
	
	public void setPosition(StreamPosition pos) {
		setPosition(pos.getLine(), pos.getColumn(), pos.getStart());
	} 
	
	private Symbol symbol;
	
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public Symbol getSymbol() {
		return this.symbol;
	}
}
