package de.fu_berlin.compilerbau.symbolTable;

import java.util.List;

/**
 * This is a method of a {@link ClassOrInterface class or interface}.
 * {@link Constructor Constructors} are methods as well.
 * @author rene
 */
public interface Method extends Scope, QualifiedSymbol, Comparable<Method> {
	
	List<Variable> getParameters();
	
	/**
	 * The return type of this method.
	 * Arbitrary for {@link Constructor constructors}!
	 * @return may be null for {@link Void} and  {@link Constructor constructors}
	 */
	Symbol getReturnType();
	
	/**
	 * The outmost scope of this method.
	 * @return most likely {@code this}
	 */
	Scope getScope();
	
}
