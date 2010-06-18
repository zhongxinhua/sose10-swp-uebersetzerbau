package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.CombinedSet;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ClassImpl extends ClassOrInterfaceImpl implements Class {
	
	protected final Map<Member,MemberImpl> members = new TreeMap<Member,MemberImpl>();
	protected final Map<Constructor, ConstructorImpl> ctors = new TreeMap<Constructor, ConstructorImpl>();
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);
		;
	protected final Symbol extends_;

	public ClassImpl(Runtime runtime, SymbolContainer parent, Symbol extends_, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) throws InvalidIdentifierException {
		super(runtime, parent, implements_, modifier, canonicalName);
		this.extends_ = extends_;
	}

	@Override
	public Constructor addConstructor(StreamPosition pos, Iterator<Symbol> parameters,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException, InvalidIdentifierException {
		final ConstructorImpl newSymbol = new ConstructorImpl(getRuntime(), this, pos, parameters, modifier);
		final ConstructorImpl oldSymbol = ctors.get(newSymbol);
		if(oldSymbol != null) {
			throw new DuplicateIdentifierException(this, newSymbol, oldSymbol);
		}
		ctors.put(newSymbol, newSymbol);
		return newSymbol;
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		// TODO type
		final MemberImpl newSymbol = new MemberImpl(getRuntime(), this, name, modifier);
		final MemberImpl oldSymbol = members.get(newSymbol);
		if(oldSymbol != null) {
			throw new DuplicateIdentifierException(this, newSymbol, oldSymbol);
		}
		shadowedSymbols.test(name, newSymbol);
		members.put(newSymbol, newSymbol);
		return newSymbol;
	}

	@Override
	public Symbol getSuperClass() {
		return extends_;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CLASS;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Symbol> getContainedSymbols() {
		Set[] sets = new Set[3];
		sets[0] = members.keySet();
		sets[1] = ctors.keySet();
		sets[2] = super.getContainedSymbols();
		return new CombinedSet<Symbol>(sets);
	}
	
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return shadowedSymbols.list;
	}

}
