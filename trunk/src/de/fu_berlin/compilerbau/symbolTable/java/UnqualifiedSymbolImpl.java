package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.StreamPosition;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;

class UnqualifiedSymbolImpl implements UnqualifiedSymbol, Comparable<Symbol> {
	
	protected final Map<SymbolType, Likelyness> likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
	protected final PositionString call;
	protected final SymbolContainer container;

	protected SymbolImpl storageSymbol;
	
	static final EnumMap<SymbolType,SymbolType[]> REPLICATIONS = new EnumMap<SymbolType,SymbolType[]>(SymbolType.class);
	static {
		REPLICATIONS.put(CLASS_OR_INTERFACE, new SymbolType[] { PRIMITIVE_TYPE, CLASS, INTERFACE, ARRAY_TYPE, VOID });
		REPLICATIONS.put(METHOD,             new SymbolType[] { CONSTRUCTOR });
		REPLICATIONS.put(SCOPE,              new SymbolType[] { RUNTIME, METHOD, PACKAGE });
		REPLICATIONS.put(VARIABLE,           new SymbolType[] { MEMBER });
	}

	UnqualifiedSymbolImpl(PositionString call, Runtime runtime, SymbolContainer container) {
		this.storageSymbol = new SymbolImpl(runtime, null);
		this.call = call;
		this.container = container;
		
		for(SymbolType t : SymbolType.values()) {
			likelyness.put(t, MAYBE);
		}
	}

	UnqualifiedSymbolImpl(PositionString call, Runtime runtime, SymbolContainer container, Iterator<Entry<SymbolType, Likelyness>> likeliness_) {
		this(call, runtime, container);
		
		while(likeliness_.hasNext()) {
			Entry<SymbolType, Likelyness> next = likeliness_.next();
			likelyness.put(next.getKey(), next.getValue());
		}
	}

	UnqualifiedSymbolImpl(PositionString call, Runtime runtime, SymbolContainer container,
			Map<SymbolType, Likelyness> likelynesses) {
		this.storageSymbol = new SymbolImpl(runtime, (Package)null);
		this.call = call;
		this.container = container;
		likelyness.putAll(likelynesses);
	}

	//*****************************************************************************
	// UnqualifiedSymbol:
	//*****************************************************************************

	@Override
	public Likelyness is(SymbolType what) {
		Likelyness result = likelyness.get(what);
		return result != null ? result : Likelyness.MAYBE;
	}
	
	@Override
	public PositionString getCall() {
		return call;
	}

	@Override
	public Map<SymbolType, Likelyness> getLikelynessPerType() {
		return likelyness;
	}

	@Override
	public SymbolContainer getContainer() {
		return container;
	}

	//*****************************************************************************
	// Symbol:
	//*****************************************************************************
	
	@Override
	public String toString() {
		return "<~" + call + "~>";
	}

	@Override
	public void addMention(Symbol who, StreamPosition where) {
		storageSymbol.addMention(who, where);
	}

	@Override
	public Set<Entry<Symbol, StreamPosition>> getMentions() {
		return storageSymbol.getMentions();
	}

	@Override
	public SymbolContainer getParent() {
		return storageSymbol.getParent();
	}

	@Override
	public Runtime getRuntime() {
		return storageSymbol.getRuntime();
	}

	@Override
	public Boolean hasType(SymbolType leftType) {
		return storageSymbol.hasType(leftType);
	}

	@Override
	public int compareTo(Symbol right) {
		return compare(this, right);
	}

	public static int compare(Symbol left, Symbol right) {
		final String lStr, rStr;
		
		if(left.hasType(SymbolType.CLASS_OR_INTERFACE) != null) {
			lStr = ((QualifiedSymbol)left).getDestinationName();
		} else {
			lStr = ((UnqualifiedSymbol)left).getCall().toString();
		}
		
		if(right.hasType(SymbolType.CLASS_OR_INTERFACE) != null) {
			rStr = ((QualifiedSymbol)right).getDestinationName();
		} else {
			rStr = ((UnqualifiedSymbol)right).getCall().toString();
		}
		
		return lStr.compareTo(rStr);
	}

}
