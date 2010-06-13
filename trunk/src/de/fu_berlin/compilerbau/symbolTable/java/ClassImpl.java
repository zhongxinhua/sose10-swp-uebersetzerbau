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
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class ClassImpl extends ClassOrInterfaceImpl implements Class {
	
	protected Map<Member,MemberImpl> members = new TreeMap<Member,MemberImpl>();

	public ClassImpl(Runtime runtime, SymbolContainer parent, Symbol extends_, Iterator<Symbol> implements_,
			Modifier modifier, PositionString canonicalName) {
		// TODO extends_
		super(runtime, parent, implements_, modifier, canonicalName);
	}

	@Override
	public Constructor addConstructor(Iterator<Symbol> parameters,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		// TODO name, type, modifier
		final MemberImpl newMember = new MemberImpl(getRuntime(), this);
		final MemberImpl oldMember = members.get(newMember);
		if(oldMember != null) {
			throw new DuplicateIdentifierException(this, newMember, oldMember);
		}
		members.put(newMember, newMember);
		return newMember;
	}

	@Override
	public Symbol lookup(UnqualifiedSymbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

}
