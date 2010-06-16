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
	final PositionString call;

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime, Iterator<Entry<SymbolType, Likelyness>> likeliness) {
		super(runtime, null);
		this.call = call;
		
		likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
		while(likeliness.hasNext()) {
			Entry<SymbolType, Likelyness> next = likeliness.next();
			likelyness.put(next.getKey(), next.getValue());
		}
		
		Likelyness isVoid = likelyness.get(SymbolType.VOID);
		Likelyness isPrimitive = likelyness.get(SymbolType.PRIMITIVE_TYPE);
		if(isVoid.compareTo(isPrimitive) > 0) {
			isPrimitive = isVoid;
			likelyness.put(SymbolType.PRIMITIVE_TYPE, isPrimitive);
		}
		Likelyness isClass = likelyness.get(SymbolType.CLASS);
		if(isPrimitive.compareTo(isClass) > 0) {
			isClass = isPrimitive;
			likelyness.put(SymbolType.CLASS, isClass);
		}
		Likelyness isCoI = likelyness.get(SymbolType.CLASS_OR_INTERFACE);
		if(isClass.compareTo(isCoI) > 0) {
			isCoI = isClass;
			likelyness.put(SymbolType.CLASS_OR_INTERFACE, isCoI);
		}
		Likelyness isInterface = likelyness.get(SymbolType.INTERFACE);
		if(isInterface.compareTo(isCoI) > 0) {
			isCoI = isInterface;
			likelyness.put(SymbolType.CLASS_OR_INTERFACE, isCoI);
		}
		Likelyness isArray = likelyness.get(SymbolType.ARRAYTYPE);
		if(isArray.compareTo(isCoI) > 0) {
			isCoI = isArray;
			likelyness.put(SymbolType.CLASS_OR_INTERFACE, isCoI);
		}

		Likelyness isVariable = likelyness.get(SymbolType.VARIABLE);
		Likelyness isMember = likelyness.get(SymbolType.MEMBER);
		if(isMember.compareTo(isVariable) > 0) {
			isVariable = isMember;
			likelyness.put(SymbolType.VARIABLE, isVariable);
		}

		Likelyness isMethod = likelyness.get(SymbolType.METHOD);
		Likelyness isCtor = likelyness.get(SymbolType.CONSTRUCTOR);
		if(isMethod.compareTo(isCtor) > 0) {
			isMethod = isCtor;
			likelyness.put(SymbolType.METHOD, isCtor);
		}

		Likelyness isScope = likelyness.get(SymbolType.SCOPE);
		Likelyness isPkg = likelyness.get(SymbolType.PACKAGE);
		if(isScope.compareTo(isPkg) > 0) {
			isScope = isPkg;
			likelyness.put(SymbolType.SCOPE, isScope);
		}
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime,
			Map<SymbolType, Likelyness> likelynesses) {
		super(runtime, null);
		this.call = call;
		this.likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
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
