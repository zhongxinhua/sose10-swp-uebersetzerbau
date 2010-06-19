package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class MemberImpl extends VariableImpl implements Member {

	public MemberImpl(Runtime runtime, ClassOrInterface parent,
			PositionString name, Symbol variableType, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent, name, variableType, modifier);
	}

	@Override
	public SymbolType getType() {
		return SymbolType.MEMBER;
	}

}
