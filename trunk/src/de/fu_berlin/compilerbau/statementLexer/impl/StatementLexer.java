package de.fu_berlin.compilerbau.statementLexer.impl;

import java.util.Iterator;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.util.PositionCharacterStream;

public class StatementLexer {
	
	public static Iterable<StatementNode> tokenize(PositionCharacterStream stream) {
		final StatementNodeIterator result = new StatementNodeIterator(stream);
		return new Iterable<StatementNode>() {
			@Override
			public Iterator<StatementNode> iterator() {
				return result;
			}
		};
	}
	
}
