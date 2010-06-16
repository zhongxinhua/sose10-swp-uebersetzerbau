package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class ClassImpl extends ClassOrInterfaceImpl implements Class {
	
	protected final Map<Member,MemberImpl> members = new TreeMap<Member,MemberImpl>();
	protected final Map<Constructor, ConstructorImpl> ctors = new TreeMap<Constructor, ConstructorImpl>();
	protected final Symbol extends_;

	public ClassImpl(Runtime runtime, SymbolContainer parent, Symbol extends_, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) {
		super(runtime, parent, implements_, modifier, canonicalName);
		this.extends_ = extends_;
	}

	@Override
	public Constructor addConstructor(Iterator<Symbol> parameters,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		final ConstructorImpl newSymbol = new ConstructorImpl(getRuntime(), this, parameters, modifier);
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
			WrongModifierException {
		// TODO type
		final MemberImpl newMember = new MemberImpl(getRuntime(), this, name, modifier);
		final MemberImpl oldMember = members.get(newMember);
		if(oldMember != null) {
			throw new DuplicateIdentifierException(this, newMember, oldMember);
		}
		members.put(newMember, newMember);
		return newMember;
	}

	@Override
	public Symbol getSuperClass() {
		return extends_;
	}

	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CLASS;
	}

}
