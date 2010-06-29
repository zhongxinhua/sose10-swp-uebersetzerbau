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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
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
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PairIterator;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class MethodImpl extends ScopeImpl implements Method {
	
	protected final ClassOrInterface parent;
	protected final PositionString name;
	protected final String destionationName;
	protected Symbol resultType;
	protected final List<Variable> parameters = new LinkedList<Variable>();
	protected final Modifier modifier;

	public MethodImpl(Runtime runtime, ClassOrInterface parent, PositionString name, Symbol resultType,
			Iterator<Variable> parameters, Modifier modifier) throws InvalidIdentifierException {
		super(runtime, parent);
		this.parent = parent;
		this.name = name;
		if(name != null && !name.equals(ConstructorImpl.INIT)) {
			this.destionationName = runtime.mangleName(name.toString());
			if(destionationName == null) {
				throw new InvalidIdentifierException(this, name);
			}
		} else {
			this.destionationName = name.toString();
		}
		this.resultType = resultType;
		this.modifier = modifier;
		if(parameters != null) {
			while(parameters.hasNext()) {
				final Variable next = parameters.next();
				this.parameters.add(next);
				variables.put(next, (VariableImpl) next);
			}
		}
		
		if(resultType != null && resultType.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
			ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
				
				@Override
				public ReplaceFunResult replace()
						throws DuplicateIdentifierException,
						ShadowedIdentifierException,
						WrongModifierException,
						InvalidIdentifierException {
					final QualifiedSymbol qualifiedSymbol = ((UnqualifiedSymbol)MethodImpl.this.resultType).qualify();
					if(qualifiedSymbol != null) {
						MethodImpl.this.resultType = qualifiedSymbol;
						return ReplaceFunResult.REPLACED;
					} else {
						return ReplaceFunResult.NOT_REPLACED;
					}
				}
				
			};
			runtime.getUnqualifiedSymbolsMap().addUnqualifiedSymbol((UnqualifiedSymbol) resultType, replaceFunc);
		}
	}

	@Override
	public List<Variable> getParameters() {
		return parameters;
	}

	@Override
	public Symbol getReturnType() {
		return resultType;
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
		return SymbolType.METHOD;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public String getDestinationName() {
		return destionationName;
	}

	@Override
	public Scope getScope() {
		return this;
	}
	
	static final Comparator<? super Method> COMPARATOR = new Comparator<Method>() {

		@Override
		public int compare(Method left, Method right) {
			final int EQUAL = 0, LEFT_BIGGER = 1, RIGHT_BIGGER = -1;
			
			int result = left.getDestinationName().compareTo(right.getDestinationName());
			if(result != 0) {
				return result;
			}
			final Iterator<Variable> lIter = left.getParameters().iterator();
			final Iterator<Variable> rIter = right.getParameters().iterator();
			for(;;) {
				if(!lIter.hasNext()) {
					return rIter.hasNext() ? RIGHT_BIGGER : EQUAL;
				} else if(!rIter.hasNext()) {
					return LEFT_BIGGER;
				}
				final Symbol l = lIter.next().getVariableType();
				final Symbol r = rIter.next().getVariableType();
				
				result = UnqualifiedSymbolImpl.compare(l, r);
				if(result != 0) {
					return result;
				}
			}
		}
		
	};

	@Override
	public Comparator<? super Method> comparator() {
		return COMPARATOR;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(resultType.toString());
		builder.append(' ');
		builder.append(parent.getDestinationName());
		builder.append('.');
		builder.append(destionationName);
		builder.append('(');
		if(!parameters.isEmpty()) {
			Iterator<Variable> i = parameters.iterator();
			builder.append(i.next());
			while(i.hasNext()) {
				builder.append(", ");
				builder.append(i.next());
			}
		}
		builder.append(')');
		return builder.toString();
	}

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol)
			throws InvalidIdentifierException {
		return null;
	}

	@Override
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol == null) {
			return null;
		}
		if(symbol.is(SymbolType.VARIABLE) != Likelyness.IMPOSSIBLE) {
			final VariableImpl search = new VariableImpl(getRuntime(), this, symbol.getCall(), null, null);
			final VariableImpl result = variables.get(search);
			if(result != null) {
				return result;
			}
		}
		return getParent().lookTreeUp(symbol);
	}

	@Override
	public String getCanonicalDestinationName() {
		return ((ClassOrInterface)getParent()).getCanonicalDestinationName() + "." + getDestinationName();
	}

	@Override
	public Boolean isCompatatibleToParameters(Iterable<Symbol> parameterTypes) throws InvalidIdentifierException {
		for(Entry<Variable, Symbol> params : new PairIterator<Variable, Symbol>(parameters, parameterTypes)) {
			final Variable methodVariable = params.getKey();
			final Symbol expectedType = params.getValue();
			if(methodVariable == null || expectedType == null) { // the parameters to not have the same length
				return Boolean.FALSE;
			}
			final Symbol methodType = methodVariable.getVariableType();
			
			final ClassOrInterface methodVariableCOI, expectedTypeCOI;
			
			if(methodType.hasType(SymbolType.CLASS_OR_INTERFACE) == Boolean.TRUE) {
				expectedTypeCOI = (ClassOrInterface) methodType;
			} else {
				expectedTypeCOI = (ClassOrInterface) ((UnqualifiedSymbol)methodType).qualify(SymbolType.CLASS_OR_INTERFACE);
			}
			
			if(expectedType.hasType(SymbolType.CLASS_OR_INTERFACE) == Boolean.TRUE) {
				methodVariableCOI = (ClassOrInterface) expectedType;
			} else {
				methodVariableCOI = (ClassOrInterface) ((UnqualifiedSymbol)expectedType).qualify(SymbolType.CLASS_OR_INTERFACE);
			}
			
			if(methodVariableCOI == null || expectedTypeCOI == null) {
				return null;
			} else if(!methodVariableCOI.canBeCastInto(expectedTypeCOI)) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}

}
