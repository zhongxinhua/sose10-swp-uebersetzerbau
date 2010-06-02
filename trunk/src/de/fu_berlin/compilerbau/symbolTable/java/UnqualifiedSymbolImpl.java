package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;

public class UnqualifiedSymbolImpl extends SymbolImpl implements
		UnqualifiedSymbol {
	
	final Map<SymbolType, Likelyness> likelyness;
	final PositionString name;

	UnqualifiedSymbolImpl(PositionString name, RuntimeImpl runtime, Iterator<Entry<SymbolType, Likelyness>> likeliness) {
		super(runtime);
		this.name = name;
		
		likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
		while(likeliness.hasNext()) {
			Entry<SymbolType, Likelyness> next = likeliness.next();
			likelyness.put(next.getKey(), next.getValue());
		}
	}

	@Override
	public Likelyness is(SymbolType what) {
		Likelyness result = likelyness.get(what);
		return result != null ? result : Likelyness.MAYBE;
	}
	
	@Override
	public String toString() {
		return name.toString();
	}

}
