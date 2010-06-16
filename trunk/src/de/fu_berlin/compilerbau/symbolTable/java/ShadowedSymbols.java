package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.PositionString;

class ShadowedSymbols {
	
	public final Map<QualifiedSymbol, Set<Symbol>> list = new TreeMap<QualifiedSymbol, Set<Symbol>>();
	protected final Map<String, Set<Symbol>> names = new HashMap<String, Set<Symbol>>();
	protected final SymbolContainerImpl container;
	
	public ShadowedSymbols(SymbolContainerImpl container) {
		this.container = container;
	}

	private static final long serialVersionUID = -8115798161952915022L;
	
	private Set<Symbol> put(QualifiedSymbol newSymbol, Symbol oldSymbol) {
		String name = newSymbol.getCanonicalName();
		Set<Symbol> result = names.get(name);
		if(result == null) {
			result = new TreeSet<Symbol>();
			list.put(newSymbol, result);
			names.put(name, result);
		}
		result.add(oldSymbol);
		return result;
	}
	
	public void test(PositionString name, QualifiedSymbol newSymbol) throws ShadowedIdentifierException {
		final Runtime rt = container.getRuntime();;
		final Symbol shadowed = container.lookup(rt.getUnqualifiedSymbol(name));
		if(shadowed != null) {
			if(rt.getThrowsAtShadowing()) {
				throw new ShadowedIdentifierException(container, newSymbol, shadowed);
			}
			put(newSymbol, shadowed);
			rt.getAllShadowsList().add(new Pair<QualifiedSymbol, Symbol>(newSymbol, shadowed));
		}
	}

}
