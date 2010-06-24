package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Member;
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
import de.fu_berlin.compilerbau.util.Pair;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ArrayTypeImpl extends ClassImpl implements ArrayType {
	
	protected Symbol componentType;
	protected final int dimension;

	public ArrayTypeImpl(Runtime runtime, Symbol componentType, int dimension) throws InvalidIdentifierException {
		super(runtime, runtime.getUndefinedScope(), null, null, null, null);
		this.componentType = componentType;
		this.dimension = dimension;

		if(componentType.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
			ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
				
				@Override
				public ReplaceFunResult replace()
						throws DuplicateIdentifierException,
						ShadowedIdentifierException,
						WrongModifierException,
						InvalidIdentifierException {
					final SymbolContainer container = ((UnqualifiedSymbol)ArrayTypeImpl.this.componentType).getContainer();
					final PositionString call = ((UnqualifiedSymbol)ArrayTypeImpl.this.componentType).getCall();
					final QualifiedSymbol qualifiedSymbol = container.getQualifiedSymbol(call, SymbolType.CLASS_OR_INTERFACE);
					if(qualifiedSymbol != null) {
						ArrayTypeImpl.this.componentType = qualifiedSymbol;
						return ReplaceFunResult.REPLACED;
					} else {
						return ReplaceFunResult.NOT_REPLACED;
					}
				}
				
			};
			runtime.getUnqualifiedSymbolsMap().addUnqualifiedSymbol((UnqualifiedSymbol) componentType, replaceFunc);
		}
	}

	public ArrayTypeImpl(Runtime rt, Class<?> clazz) throws InvalidIdentifierException {
		this(rt, arrayClassInfo(rt,clazz).getKey(), arrayClassInfo(rt,clazz).getValue());
	}

	private static Pair<Symbol, Integer> arrayClassInfo(Runtime runtime, Class<?> clazz) throws InvalidIdentifierException {
		int i = 0;
		Class<?> ct;
		while((ct = clazz.getComponentType()) != null) {
			clazz = ct;
			++i;
		}
		final Symbol symbol = RuntimeFactory.javaToCompilerType(runtime, clazz);
		return new Pair<Symbol, Integer>(symbol, Integer.valueOf(i));
	}

	@Override
	public Constructor addConstructor(StreamPosition pos,
			Iterator<Variable> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Symbol getSuperClass() {
		return null;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.PRIMITIVE_TYPE;
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return Collections.emptyMap();
	}

	@Override
	public Symbol getComponentType() {
		return componentType;
	}

	@Override
	public int getDimension() {
		return dimension;
	}
	
	@Override
	public String getDestinationName() {
		final StringBuilder builder = new StringBuilder();
		final Boolean hasTypeCoI = componentType.hasType(SymbolType.CLASS_OR_INTERFACE);
		if(hasTypeCoI == Boolean.TRUE) {
			builder.append(((QualifiedSymbol)componentType).getCanonicalDestinationName());
		} else if(hasTypeCoI == null) {
			builder.append(((UnqualifiedSymbol)componentType).getCall());
		} else {
			throw new RuntimeException("Symbol is not a class or interface.");
		}
		for(int i = 0; i < dimension; ++i) {
			builder.append("[]");
		}
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return getDestinationName();
	}

	@Override
	public String getCanonicalDestinationName() {
		return getDestinationName();
	}

}
