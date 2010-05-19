package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;

public class Tokenizer implements Iterator<StatementNode> {
	
	protected StatementNode nextNodeToRead;
	protected PositionCharacterStream stream;
	
	Tokenizer(PositionCharacterStream stream) {
		this.stream = stream;
	}
	
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	protected StatementNode primitiveGetNext() {
		while(stream.hasNext()) {
			Character next = stream.next();
			if(!Character.isWhitespace(next)) {
				stream.pushback(next);
				break;
			}
		}
		if(!stream.hasNext()) {
			return null;
		}
		
		final int line = stream.getLine();
		final int start = stream.getStart();
		final int character = stream.getCharacter();
		
		// TODO
		
		return null;
	}

	@Override
	public boolean hasNext() {
		if(nextNodeToRead != null) {
			return true;
		}
		nextNodeToRead = primitiveGetNext();
		return nextNodeToRead != null;
	}

	@Override
	public StatementNode next() throws NoSuchElementException {
		if(hasNext()) {
			final StatementNode result = nextNodeToRead;
			nextNodeToRead = null;
			return result;
		}
		throw new NoSuchElementException();
	}
	
}
