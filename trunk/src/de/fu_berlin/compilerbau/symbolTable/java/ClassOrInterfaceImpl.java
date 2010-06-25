package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
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
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

class ClassOrInterfaceImpl extends SymbolContainerImpl implements ClassOrInterface {
	
	protected final PositionString name;
	protected final String destinationName;
	protected final Modifier modifier;
	protected final Set<Symbol> interfaces = new TreeSet<Symbol>();
	protected final Map<Method, MethodImpl> methods = new TreeMap<Method, MethodImpl>(MethodImpl.COMPARATOR);
	protected final ShadowedSymbols shadowedSymbols = new ShadowedSymbols(this);

	public ClassOrInterfaceImpl(Runtime runtime, Package parent, Iterator<Symbol> implements_,
			Modifier modifier, PositionString name) throws InvalidIdentifierException {
		super(runtime, parent);
		this.modifier = modifier;
		this.name = name;
		if(name != null) {
			this.destinationName = runtime.mangleName(name.toString());
			if(
					(getType() == SymbolType.CLASS || getType() == SymbolType.INTERFACE) &&
					(this.destinationName == null || !runtime.isValidIdentifier(this.destinationName))) {
				throw new InvalidIdentifierException(this, name);
			}
		} else {
			this.destinationName = null;
		}
		if(implements_ != null) {
			UnqualifiedSymbolsMap<UnqualifiedSymbol> unqualifiedSymbolsMap = runtime.getUnqualifiedSymbolsMap();
			while(implements_.hasNext()) {
				final Symbol next = implements_.next();
				if(next.hasType(SymbolType.INTERFACE) == null) {
					ReplaceFunc replaceFunc = new UnqualifiedSymbolsMap.ReplaceFunc() {
						
						@Override
						public ReplaceFunResult replace()
								throws DuplicateIdentifierException,
								ShadowedIdentifierException,
								WrongModifierException,
								InvalidIdentifierException {
							final QualifiedSymbol qualifiedSymbol = ((UnqualifiedSymbol)next).qualify();
							if(qualifiedSymbol != null) {
								interfaces.remove(next);
								interfaces.add(qualifiedSymbol);
								return ReplaceFunResult.REPLACED;
							} else {
								return ReplaceFunResult.NOT_REPLACED;
							}
						}
						
					};
					unqualifiedSymbolsMap.addUnqualifiedSymbol((UnqualifiedSymbol) next, replaceFunc);
				}
				interfaces.add(next);
			}
		}
	}

	@Override
	public Method addMethod(PositionString name, Symbol resultType,
			Iterator<Variable> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		final MethodImpl newSymbol = new MethodImpl(getRuntime(), this, name, resultType, parameters, modifier);
		final MethodImpl oldSymbol = methods.get(newSymbol);
		if(oldSymbol != null) {
			throw new DuplicateIdentifierException(this, newSymbol, oldSymbol);
		}
		shadowedSymbols.test(name, newSymbol);
		methods.put(newSymbol, newSymbol);
		return newSymbol;
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
	public Set<Symbol> getImplementedInterfaces() {
		return interfaces;
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return shadowedSymbols.list;
	}

	@Override
	public SymbolType getType() {
		return SymbolType.CLASS_OR_INTERFACE;
	}

	@Override
	public QualifiedSymbol lookTreeUp(UnqualifiedSymbol symbol) throws InvalidIdentifierException {
		if(symbol.is(SymbolType.METHOD) != Likelyness.IMPOSSIBLE) {
			MethodImpl oldsymbol = new MethodImpl(getRuntime(), this, symbol.getCall(), null, null, null);
			MethodImpl result = methods.get(oldsymbol);
			if(result != null) {
				return result;
			}
		}
		return getRuntime().lookTreeUp(symbol);
	}

	@Override
	public String getDestinationName() {
		return destinationName;
	}

	@Override
	public QualifiedSymbol lookTreeDown(UnqualifiedSymbol symbol)
			throws InvalidIdentifierException {
		return null;
	}
	
	protected static final Comparator<? super ClassOrInterface> COMPARATOR = new Comparator<ClassOrInterface>() {

		@Override
		public int compare(ClassOrInterface left, ClassOrInterface right) {
			return left.getDestinationName().compareTo(right.getDestinationName());
		}
		
	};

	@Override
	public Comparator<? super ClassOrInterface> comparator() {
		return COMPARATOR;
	}

	@Override
	public String getCanonicalDestinationName() {
		return ((Package)getParent()).getCanonicalDestinationName() + "." + getDestinationName();
	}

	@Override
	public boolean isSame(ClassOrInterface right) throws InvalidIdentifierException {
		if(getType() != right.getType()) {
			return false;
		}
		switch(getType()) {
			case INTERFACE:
			case CLASS:
				return COMPARATOR.compare(this, (ClassOrInterface)right) == 0;
			case ARRAY_TYPE:
				if(((ArrayType)this).getDimension() != ((ArrayType)right).getDimension()) {
					return false;
				}
				Symbol leftCT = ((ArrayType)this).getComponentType();
				Symbol rightCT = ((ArrayType)this).getComponentType();
				if(leftCT.hasType(SymbolType.CLASS_OR_INTERFACE) != null) {
					if(rightCT.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
						rightCT = ((UnqualifiedSymbol)rightCT).qualify();
					}
					return COMPARATOR.compare((ClassOrInterface)leftCT, (ClassOrInterface)rightCT) == 0;
				} else {
					if(rightCT.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
						return leftCT.compareTo(rightCT) == 0;
					} else if(rightCT.hasType(SymbolType.CLASS_OR_INTERFACE) == null) {
						leftCT = ((UnqualifiedSymbol)leftCT).qualify();
					}
					return COMPARATOR.compare((ClassOrInterface)leftCT, (ClassOrInterface)rightCT) == 0;
				}
			case PRIMITIVE_TYPE:
				return this == right; // there is just one instance per primitive type
			case VOID:
				return true; // there is just one instance of void
			default:
				throw new RuntimeException("Not a valid class or interface: " + getType());
		}
	}

}
