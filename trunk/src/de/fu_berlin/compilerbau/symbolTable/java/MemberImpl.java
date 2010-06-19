package de.fu_berlin.compilerbau.symbolTable.java;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.PositionString;

class MemberImpl extends VariableImpl implements Member {

	protected final int COMPARE_KEY;

	public MemberImpl(Runtime runtime, ClassOrInterface parent,
			PositionString name, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent, name, modifier);
		this.COMPARE_KEY = (parent.getDestinationName() + "." + getDestinationName()).hashCode();
	}

	@Override
	public SymbolType getType() {
		return SymbolType.MEMBER;
	}

	@Override
	public int compareKey() {
		return COMPARE_KEY;
	}

}
