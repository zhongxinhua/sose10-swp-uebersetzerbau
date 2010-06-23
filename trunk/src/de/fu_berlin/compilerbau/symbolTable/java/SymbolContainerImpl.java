package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Iterator;
import java.util.Map;

import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.UnqualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.Likelyness;
import de.fu_berlin.compilerbau.util.PositionString;


abstract class SymbolContainerImpl extends SymbolImpl implements SymbolContainer {
	
	SymbolContainerImpl(Runtime runtime, SymbolContainer parent) {
		super(runtime, parent);
		if(runtime != null) {
			runtime.registerSymbolContainer(this);
		}
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, this, type);
		return lookTreeUp(uniqualifiedSymbol);
	}
	
	@Override
	public QualifiedSymbol getQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		Runtime runtime = getRuntime();
		UnqualifiedSymbol uniqualifiedSymbol = runtime.getUnqualifiedSymbol(name, this);
		return lookTreeUp(uniqualifiedSymbol);
	}
	
	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, Iterator<Map.Entry<SymbolType,Likelyness>> likeliness) throws InvalidIdentifierException{
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this, likeliness);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}
	
	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name, SymbolType type) throws InvalidIdentifierException {
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this, type);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}

	@Override
	public Symbol tryGetQualifiedSymbol(PositionString name) throws InvalidIdentifierException {
		final UnqualifiedSymbol unqualifiedSymbol = getRuntime().getUnqualifiedSymbol(name, this);
		final QualifiedSymbol qualifiedSymbol = lookTreeUp(unqualifiedSymbol);
		if(qualifiedSymbol != null) {
			return qualifiedSymbol;
		} else {
			return unqualifiedSymbol;
		}
	}
	
}
