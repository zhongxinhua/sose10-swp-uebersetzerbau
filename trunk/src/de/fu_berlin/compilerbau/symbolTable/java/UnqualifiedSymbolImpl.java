/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.StreamPosition;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;

class UnqualifiedSymbolImpl implements UnqualifiedSymbol, Comparable<Symbol> {
	
	protected final Map<SymbolType, Likelyness> likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
	protected final PositionString call;
	protected final SymbolContainer container;
	protected QualifiedSymbol qualifiedSymbol;

	protected SymbolImpl storageSymbol;
	
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
	
	@Override
	public QualifiedSymbol qualify() throws InvalidIdentifierException {
		if(qualifiedSymbol == null) {
			qualifiedSymbol = container.getQualifiedSymbol(call);
		}
		return qualifiedSymbol;
	}
	
	@Override
	public QualifiedSymbol qualify(SymbolType type) throws InvalidIdentifierException {
		if(qualifiedSymbol == null) {
			qualifiedSymbol = container.getQualifiedSymbol(call, type);
		}
		return qualifiedSymbol;
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
