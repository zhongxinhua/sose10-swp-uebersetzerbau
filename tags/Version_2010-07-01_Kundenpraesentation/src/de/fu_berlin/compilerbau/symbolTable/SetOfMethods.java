package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;
import java.util.Set;

import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;

public interface SetOfMethods extends QualifiedSymbol {
	
	Set<Method> getContainedMethods();
	List<Method> getContainedMethods(Iterable<Symbol> parameterTypes) throws InvalidIdentifierException;
	
}
