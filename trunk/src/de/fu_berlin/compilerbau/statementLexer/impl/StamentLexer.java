package de.fu_berlin.compilerbau.statementLexer.impl;

public class StatementLexer {
	
	public static Iterator<StatementNode> tokenize(CharSequence statement, StreamPosition startPosition) {
		return new Tokenizer(CharSequence statement, StreamPosition startPosition);
	}
	
}
