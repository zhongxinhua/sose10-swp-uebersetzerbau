package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Comparator;

import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunResult;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbolsMap.ReplaceFunc;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class VariableImpl extends SymbolImpl implements Variable {
	
	protected final PositionString name;
	protected final Modifier modifier;
	protected final String destinationName;
	protected Symbol variableType;

	public VariableImpl(Runtime runtime, SymbolContainer parent, PositionString name, Symbol variableType, Modifier modifier)
			throws InvalidIdentifierException {
		super(runtime, parent);
		this.name = name;
		if(name != null) {
			this.destinationName = runtime.mangleName(name.toString());
			if(destinationName == null || !runtime.isValidIdentifier(destinationName)) {
				throw new InvalidIdentifierException(parent, name);
			}
		} else {
			this.destinationName = null;
		}
		this.modifier = modifier;
		this.variableType = variableType;
		
		if(variableType.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
			ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
				
				@Override
				public ReplaceFunResult replace()
						throws DuplicateIdentifierException,
						ShadowedIdentifierException,
						WrongModifierException,
						InvalidIdentifierException {
					final QualifiedSymbol qualifiedSymbol = ((UnqualifiedSymbol)VariableImpl.this.variableType).qualify();
					if(qualifiedSymbol != null) {
						VariableImpl.this.variableType = qualifiedSymbol;
						return ReplaceFunResult.REPLACED;
					} else {
						return ReplaceFunResult.NOT_REPLACED;
					}
				}
				
			};
			runtime.getUnqualifiedSymbolsMap().addUnqualifiedSymbol((UnqualifiedSymbol) variableType, replaceFunc);
		}
	}

	@Override
	public String getName() {
		return name.toString();
	}

	@Override
	public Modifier getModifier() {
		return modifier;
	}

	@Override
	public StreamPosition getPosition() {
		return name;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.VARIABLE;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}
	
	static final Comparator<? super Variable> COMPARATOR = new Comparator<Variable>() {

		@Override
		public int compare(Variable left, Variable right) {
			return left.getDestinationName().compareTo(right.getDestinationName());
		}
		
	};

	@Override
	public Comparator<? super Variable> comparator() {
		return COMPARATOR;
	}
	
	@Override
	public Symbol getVariableType() {
		return variableType;
	}
	
	@Override
	public String toString() {
		return "" + variableType + " " + name;
	}

	@Override
	public String getCanonicalDestinationName() {
		return getDestinationName();
	}

}
