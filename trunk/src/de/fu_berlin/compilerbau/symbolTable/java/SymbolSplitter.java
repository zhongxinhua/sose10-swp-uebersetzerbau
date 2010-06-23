package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.LinkedList;
import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.PositionStringBuilder;

class SymbolSplitter {
	
	public static interface QualifiedSymbolCtor {
		
		QualifiedSymbol newInstance(PositionString str) throws InvalidIdentifierException;
		
	}

	public static QualifiedSymbol lookup(
			Runtime runtime,
			SymbolContainer self,
			UnqualifiedSymbol symbol,
			Map<? extends QualifiedSymbol, ? extends SymbolContainer> containers,
			QualifiedSymbolCtor ctor
	) throws InvalidIdentifierException {
		
		final Map<SymbolType, Likelyness> likelynessPerType = symbol.getLikelynessPerType();

		final LinkedList<PositionString> list = symbol.getCall().split('.', -1);
		for(int i = list.size()-1; i > 0; --i) {
			final PositionString p0 = list.get(0);
			final PositionStringBuilder builder = new PositionStringBuilder(p0);
			builder.append(p0.toString());
			for(int h = 1; h < i; ++h) {
				builder.append('.');
				builder.append(list.get(h));
			}
			
			final QualifiedSymbol qs = ctor.newInstance(builder.toPositionString());
			final SymbolContainer container = containers.get(qs);
			if(container != null) {
				final PositionString p1 = list.get(i);
				final PositionStringBuilder subCall = new PositionStringBuilder(p1);
				subCall.append(p1);
				for(int h = i+1; h < list.size()-1; ++h) {
					subCall.append('.');
					subCall.append(list.get(h));
				}
				
				final PositionString subCallStr = subCall.toPositionString();
				final UnqualifiedSymbolImpl subCallSym =
						new UnqualifiedSymbolImpl(subCallStr, runtime, self, likelynessPerType);
				return container.lookTreeDown(subCallSym);
			}
		}
		
		return null;
		
	}
	
}
