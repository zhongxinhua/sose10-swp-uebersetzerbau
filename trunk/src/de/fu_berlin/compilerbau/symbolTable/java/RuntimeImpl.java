package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.PositionString;


public class RuntimeImpl extends SymbolContainerImpl implements Runtime {
	
	protected final Set<Package> packages = new TreeSet<Package>();
	
	@Override
	public Package addPackage(PositionString name, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException, WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<Package> getPackages() {
		return packages;
	}
	
	protected boolean throwsAtShadowing = false;
	
	@Override
	public boolean getThrowsAtShadowing() {
		return throwsAtShadowing;
	}
	
	@Override
	public void setThrowsAtShadowing(boolean throwsAtShadowing) {
		this.throwsAtShadowing = throwsAtShadowing;
	}
	
	@Override
	public UnqualifiedSymbol getUniqualifiedSymbol(PositionString name,
			Iterator<Pair<SymbolType, Likelyness>> likeliness) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UnqualifiedSymbol getUniqualifiedSymbol(PositionString name, final SymbolType type) {
		Iterator<Pair<SymbolType, Likelyness>> iterator = new Iterator<Pair<SymbolType, Likelyness>>() {
			int current = 0;
			
			@Override
			public boolean hasNext() {
				return current < SymbolType.values().length;
			}
			
			@Override
			public Pair<SymbolType, Likelyness> next() throws NoSuchElementException {
				SymbolType[] values = SymbolType.values();
				if(values.length >= current) {
					throw new NoSuchElementException();
				}
				SymbolType symbolType = values[current];
				return new Pair<SymbolType, Likelyness>(symbolType, symbolType == type ?
						Likelyness.YES : Likelyness.IMPOSSIBLE);
			}
			
			@Override
			public void remove() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}
		};
		return getUniqualifiedSymbol(name, iterator);
	}
	
}
