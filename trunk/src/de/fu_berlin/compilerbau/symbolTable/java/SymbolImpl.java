package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.util.StreamPosition;


class SymbolImpl implements Symbol {
	
	private final Runtime runtime;
	private final SymbolContainer parent;
	protected final Set<Map.Entry<Symbol, StreamPosition>> mentions = new TreeSet<Map.Entry<Symbol, StreamPosition>>();
	
	SymbolImpl(Runtime runtime, SymbolContainer parent) {
		this.runtime = runtime;
		this.parent = parent ;
	}
	
	@Override
	public void addMention(Symbol who, StreamPosition where) {
		Mention mention = new Mention(who, where);
		mentions.add(mention);
	}
	
	@Override
	public Set<Map.Entry<Symbol, StreamPosition>> getMentions() {
		return mentions;
	}
	
	@Override
	public SymbolContainer getParent() {
		return parent;
	}

	@Override
	public Runtime getRuntime() {
		return runtime;
	}
	
	@Override
	public Boolean hasType(SymbolType rightType) {
		if(!(this instanceof QualifiedSymbol)) {
			return null;
		}
		final SymbolType leftType = ((QualifiedSymbol)this).getType();
		final Boolean result = SymbolType.implicates(leftType, rightType);
		return result;
	}

	@Override
	public int compareTo(Symbol right) {
		return UnqualifiedSymbolImpl.compare(this, right);
	}
	
}
