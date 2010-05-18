package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.Iterator;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class StatementLexer {
	
	public static Iterator<StatementNode> tokenize(CharSequence statement, StreamPosition startPosition) {
		return new Tokenizer(statement, startPosition);
	}
	
}
