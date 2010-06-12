package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.util.StreamPosition;

class Mention implements Map.Entry<Symbol, StreamPosition>, Comparable<Map.Entry<Symbol, StreamPosition>> {
	
	protected final Symbol from;
	protected final StreamPosition position;
	
	public Mention(Symbol from, StreamPosition position) {
		this.position = position;
		this.from = from;
	}

	@Override
	public Symbol getKey() {
		return from;
	}

	@Override
	public StreamPosition getValue() {
		return position;
	}

	@Override
	public StreamPosition setValue(StreamPosition value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(Entry<Symbol, StreamPosition> right) {
		final int l = position.getLine() - right.getValue().getLine();
		if(l != 0) {
			return l;
		}
		final int c = position.getCharacter() - right.getValue().getCharacter();
		if(c != 0) {
			return c;
		} else if(from == right.getKey()) {
			return 0;
		} else {
			return from.toString().compareTo(right.toString());
		}
	}

}
