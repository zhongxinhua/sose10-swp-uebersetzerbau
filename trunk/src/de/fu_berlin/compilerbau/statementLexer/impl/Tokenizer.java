package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.*;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.util.StreamPosition;

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

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StatementNode next() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
