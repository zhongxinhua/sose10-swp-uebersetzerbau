package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.util.Likelyness;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;

public class UnqualifiedSymbolImpl extends SymbolImpl implements
		UnqualifiedSymbol {
	
	final Map<SymbolType, Likelyness> likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
	final PositionString call;
	
	static final EnumMap<SymbolType,SymbolType[]> REPLICATIONS = new EnumMap<SymbolType,SymbolType[]>(SymbolType.class);
	static {
		REPLICATIONS.put(CLASS_OR_INTERFACE, new SymbolType[] { PRIMITIVE_TYPE, CLASS, INTERFACE, ARRAY_TYPE, VOID });
		REPLICATIONS.put(METHOD,             new SymbolType[] { CONSTRUCTOR });
		REPLICATIONS.put(SCOPE,              new SymbolType[] { RUNTIME, METHOD, PACKAGE, SCOPE });
		REPLICATIONS.put(VARIABLE,           new SymbolType[] { MEMBER });
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime) {
		super(runtime, null);
		this.call = call;
		
		for(SymbolType t : SymbolType.values()) {
			likelyness.put(t, MAYBE);
		}
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime, Iterator<Entry<SymbolType, Likelyness>> likeliness_) {
		this(call, runtime);
		
		while(likeliness_.hasNext()) {
			Entry<SymbolType, Likelyness> next = likeliness_.next();
			likelyness.put(next.getKey(), next.getValue());
		}
		
		boolean hasChanges;
		do {
			hasChanges = false;
			for(final Entry<SymbolType, Likelyness> left : likelyness.entrySet()) {
				// left → right
				final SymbolType leftSymbol = left.getKey();
				final Likelyness leftLikelyness = left.getValue();
				if(leftLikelyness != IMPOSSIBLE) {
					for(final SymbolType rightSymbol : REPLICATIONS.get(leftSymbol)) {
						final Likelyness rightLikelyness = likelyness.get(rightSymbol);
						if(leftLikelyness.compareTo(rightLikelyness) > 0) {
							likelyness.put(rightSymbol, leftLikelyness);
							hasChanges = true;
						}
					}
				}
			}
		} while(hasChanges);
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime,
			Map<SymbolType, Likelyness> likelynesses) {
		super(runtime, null);
		this.call = call;
		likelyness.putAll(likelynesses);
	}

	@Override
	public Likelyness is(SymbolType what) {
		Likelyness result = likelyness.get(what);
		return result != null ? result : Likelyness.MAYBE;
	}
	
	@Override
	public String toString() {
		return call.toString();
	}
	
	@Override
	public PositionString getCall() {
		return call;
	}

	@Override
	public Map<SymbolType, Likelyness> getLikelynessPerType() {
		return likelyness;
	}

}
