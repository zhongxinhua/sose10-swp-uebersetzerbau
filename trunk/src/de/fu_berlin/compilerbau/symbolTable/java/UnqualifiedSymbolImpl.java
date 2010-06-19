package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Interface;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.PrimitiveType;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.Void;
import static de.fu_berlin.compilerbau.symbolTable.SymbolType.*;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.util.Visibility;
import static de.fu_berlin.compilerbau.util.Likelyness.*;
import de.fu_berlin.compilerbau.util.PositionString;

class UnqualifiedSymbolImpl implements UnqualifiedSymbol, AnySymbolType {
	
	protected final Map<SymbolType, Likelyness> likelyness = new EnumMap<SymbolType, Likelyness>(SymbolType.class);
	protected final PositionString call;

	protected AnySymbolType actualSymbol = null;
	protected SymbolImpl storageSymbol;
	
	static final EnumMap<SymbolType,SymbolType[]> REPLICATIONS = new EnumMap<SymbolType,SymbolType[]>(SymbolType.class);
	static {
		REPLICATIONS.put(CLASS_OR_INTERFACE, new SymbolType[] { PRIMITIVE_TYPE, CLASS, INTERFACE, ARRAY_TYPE, VOID });
		REPLICATIONS.put(METHOD,             new SymbolType[] { CONSTRUCTOR });
		REPLICATIONS.put(SCOPE,              new SymbolType[] { RUNTIME, METHOD, PACKAGE, SCOPE });
		REPLICATIONS.put(VARIABLE,           new SymbolType[] { MEMBER });
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime) {
		this.storageSymbol = new SymbolImpl(runtime, null);
		this.call = call;
		
		for(SymbolType t : SymbolType.values()) {
			likelyness.put(t, MAYBE);
		}
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime, Iterator<Entry<SymbolType, Likelyness>> likeliness_) {
		this(call, runtime);
		
		while(likeliness_.hasNext()) {
			Entry<SymbolType, Likelyness> next = likeliness_.next();
			likelyness.put(next.getKey(), next.getValue());
		}
		
		boolean hasChanges;
		do {
			hasChanges = false;
			for(final Entry<SymbolType, Likelyness> left : likelyness.entrySet()) {
				// left → right
				final SymbolType leftSymbol = left.getKey();
				final Likelyness leftLikelyness = left.getValue();
				if(leftLikelyness != IMPOSSIBLE) {
					SymbolType[] replications = REPLICATIONS.get(leftSymbol);
					if(replications == null) {
						continue;
					}
					for(final SymbolType rightSymbol : replications) {
						final Likelyness rightLikelyness = likelyness.get(rightSymbol);
						if(leftLikelyness.compareTo(rightLikelyness) > 0) {
							likelyness.put(rightSymbol, leftLikelyness);
							hasChanges = true;
						}
					}
				}
			}
		} while(hasChanges);
	}

	UnqualifiedSymbolImpl(PositionString call, RuntimeImpl runtime,
			Map<SymbolType, Likelyness> likelynesses) {
		this.storageSymbol = new SymbolImpl(runtime, (Package)null);
		this.call = call;
		likelyness.putAll(likelynesses);
	}

	//*****************************************************************************
	// UnqualifiedSymbol:
	//*****************************************************************************

	@Override
	public Likelyness is(SymbolType what) {
		Likelyness result = likelyness.get(what);
		return result != null ? result : Likelyness.MAYBE;
	}
	
	@Override
	public PositionString getCall() {
		return call;
	}

	@Override
	public Map<SymbolType, Likelyness> getLikelynessPerType() {
		return likelyness;
	}

	@Override
	public QualifiedSymbol getActualSymbol() {
		return actualSymbol;
	}

	@Override
	public void setActualSymbol(QualifiedSymbol actualSymbol) {
		for(Entry<Symbol, StreamPosition> mention : storageSymbol.getMentions()) {
			actualSymbol.addMention(mention.getKey(), mention.getValue());
		}
		
		this.actualSymbol = (AnySymbolType) actualSymbol;
		this.storageSymbol = null;
	}

	//*****************************************************************************
	// Symbol:
	//*****************************************************************************
	
	@Override
	public String toString() {
		if(actualSymbol != null) {
			return actualSymbol.toString();
		} else {
			return storageSymbol.toString();
		}
	}

	@Override
	public void addMention(Symbol who, StreamPosition where) {
		if(actualSymbol != null) {
			actualSymbol.addMention(who, where);
		} else {
			storageSymbol.addMention(who, where);
		}
	}

	@Override
	public Set<Entry<Symbol, StreamPosition>> getMentions() {
		if(actualSymbol != null) {
			return actualSymbol.getMentions();
		} else {
			return storageSymbol.getMentions();
		}
	}

	@Override
	public SymbolContainer getParent() {
		if(actualSymbol != null) {
			return actualSymbol.getParent();
		} else {
			return storageSymbol.getParent();
		}
	}

	@Override
	public Runtime getRuntime() {
		if(actualSymbol != null) {
			return actualSymbol.getRuntime();
		} else {
			return storageSymbol.getRuntime();
		}
	}

	@Override
	public Boolean hasType(SymbolType leftType) {
		if(actualSymbol != null) {
			return actualSymbol.hasType(leftType);
		} else {
			return null;
		}
	}
	
	//*****************************************************************************
	// AnySymbolType:
	//*****************************************************************************
	
	@Override
	public Symbol getComponentType() {
		return actualSymbol.getComponentType();
	}

	@Override
	public int getDimension() {
		return actualSymbol.getDimension();
	}

	@Override
	public Constructor addConstructor(StreamPosition pos,
			Iterator<Symbol> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addConstructor(pos, parameters, modifier);
	}

	@Override
	public Member addMember(PositionString name, Symbol type, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addMember(name, type, modifier);
	}

	@Override
	public Symbol getSuperClass() {
		return actualSymbol.getSuperClass();
	}

	@Override
	public Method addMethod(PositionString name, Symbol resultType,
			Iterator<Symbol> parameters, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addMethod(name, resultType, parameters, modifier);
	}

	@Override
	public Set<Symbol> getImplementedInterfaces() {
		return actualSymbol.getImplementedInterfaces();
	}

	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name,
			SymbolType type) throws InvalidIdentifierException {
		return actualSymbol.getQualifiedSymbol(name, type);
	}

	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name)
			throws InvalidIdentifierException {
		return actualSymbol.getQualifiedSymbol(name);
	}

	@Override
	public Set<? extends UnqualifiedSymbol> getUnqualifiedSymbols() {
		return actualSymbol.getUnqualifiedSymbols();
	}

	@Override
	public boolean hasUnqualifiedSymbols() {
		return actualSymbol.hasUnqualifiedSymbols();
	}

	@Override
	public QualifiedSymbol lookup(UnqualifiedSymbol symbol)
			throws InvalidIdentifierException {
		return actualSymbol.lookup(symbol);
	}

	@Override
	public String getDestinationName() {
		return actualSymbol.getDestinationName();
	}

	@Override
	public Modifier getModifier() {
		return actualSymbol.getModifier();
	}

	@Override
	public String getName() {
		return actualSymbol.getName();
	}

	@Override
	public StreamPosition getPosition() {
		return actualSymbol.getPosition();
	}

	@Override
	public SymbolType getType() {
		return actualSymbol.getType();
	}

	@Override
	public List<Symbol> getParameters() {
		return actualSymbol.getParameters();
	}

	@Override
	public Symbol getReturnType() {
		return actualSymbol.getReturnType();
	}

	@Override
	public Scope getScope() {
		return actualSymbol.getScope();
	}

	@Override
	public Scope addScope() {
		return actualSymbol.addScope();
	}

	@Override
	public Variable addVariable(PositionString name, Symbol type,
			Modifier modifier) throws DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException,
			InvalidIdentifierException {
		return actualSymbol.addVariable(name, type, modifier);
	}

	@Override
	public boolean isFinal() {
		return actualSymbol.isFinal();
	}

	@Override
	public boolean isNative() {
		return actualSymbol.isNative();
	}

	@Override
	public boolean isStatic() {
		return actualSymbol.isStatic();
	}

	@Override
	public Visibility visibility() {
		return actualSymbol.visibility();
	}

	@Override
	public Class addClass(PositionString name, Symbol extends_,
			Iterator<Symbol> implements_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addClass(name, extends_, implements_, modifier);
	}

	@Override
	public Interface addInterface(PositionString name,
			Iterator<Symbol> extends_, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addInterface(name, extends_, modifier);
	}

	@Override
	public Set<Class> getClasses() {
		return actualSymbol.getClasses();
	}

	@Override
	public Set<ClassOrInterface> getClassesAndInterfaces() {
		return actualSymbol.getClassesAndInterfaces();
	}

	@Override
	public Set<Interface> getInterfaces() {
		return actualSymbol.getInterfaces();
	}

	@Override
	public Package addPackage(PositionString name, Modifier modifier)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException, InvalidIdentifierException {
		return actualSymbol.addPackage(name, modifier);
	}

	@Override
	public List<Entry<QualifiedSymbol, Symbol>> getAllShadowsList() {
		return actualSymbol.getAllShadowsList();
	}

	@Override
	public ArrayType getArrayType(Symbol componentType, int dimension) {
		return actualSymbol.getArrayType(componentType, dimension);
	}

	@Override
	public ArrayType getArrayType(java.lang.Class<?> clazz) {
		return actualSymbol.getArrayType(clazz);
	}

	@Override
	public Set<Package> getPackages() {
		return actualSymbol.getPackages();
	}

	@Override
	public PrimitiveType getPrimitiveType(java.lang.Class<?> c) {
		return actualSymbol.getPrimitiveType(c);
	}

	@Override
	public boolean getThrowsAtShadowing() {
		return actualSymbol.getThrowsAtShadowing();
	}

	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name,
			Iterator<Entry<SymbolType, Likelyness>> likeliness)
			throws RuntimeException {
		return actualSymbol.getUnqualifiedSymbol(name, likeliness);
	}

	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name,
			SymbolType type) {
		return actualSymbol.getUnqualifiedSymbol(name, type);
	}

	@Override
	public UnqualifiedSymbol getUnqualifiedSymbol(PositionString name) {
		return actualSymbol.getUnqualifiedSymbol(name);
	}

	@Override
	public Void getVoid() {
		return actualSymbol.getVoid();
	}

	@Override
	public boolean isValidIdentifier(String id) {
		return actualSymbol.isValidIdentifier(id);
	}

	@Override
	public String mangleName(String name) {
		return actualSymbol.mangleName(name);
	}

	@Override
	public List<SymbolContainer> qualifyAllSymbols() {
		return actualSymbol.qualifyAllSymbols();
	}

	@Override
	public void setThrowsAtShadowing(boolean throwsAtShadowing) {
		setThrowsAtShadowing(throwsAtShadowing);
	}

	@Override
	public java.lang.Class<?> getJavaClass() {
		return actualSymbol.getJavaClass();
	}

	@Override
	public java.lang.Class<?> getWrapperClass() {
		return actualSymbol.getWrapperClass();
	}

	@Override
	public boolean isNameManglingEnabled() {
		return actualSymbol.isNameManglingEnabled();
	}

	@Override
	public void setNameManglingEnabled(boolean enabled) {
		actualSymbol.setNameManglingEnabled(enabled);
	}

	@Override
	public Package getGlobalScope() {
		return actualSymbol.getGlobalScope();
	}

	@Override
	public Map<QualifiedSymbol, Set<Symbol>> getShadowedSymbols() {
		return actualSymbol.getShadowedSymbols();
	}

}
