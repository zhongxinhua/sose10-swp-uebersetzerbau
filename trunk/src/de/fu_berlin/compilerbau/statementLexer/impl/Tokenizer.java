package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.*;

public class Tokenizer implements Iterator<StatementNode> {
	
	protected StatementNode nextNodeToRead;
	
	protected int start, line, character;
	
	Tokenizer(CharSequence statement, StreamPosition startPosition) {
		this.start = startPosition.getStart();
		this.line = startPosition.getLine();
		this.character = startPosition.getCharacter();
	}
	
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
}
