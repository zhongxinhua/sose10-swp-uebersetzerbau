package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.Iterator;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;

public class StatementLexer {
	
	public static Iterator<StatementNode> tokenize(PositionCharacterStream stream) {
		return new Tokenizer(stream);
	}
	
}
