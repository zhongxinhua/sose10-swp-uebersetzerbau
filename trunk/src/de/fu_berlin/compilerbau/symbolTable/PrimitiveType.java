package de.fu_berlin.compilerbau.symbolTable;

public interface PrimitiveType extends Class {

	/**
	 * E.g. returns <code>byte.class</code>
	 */
	java.lang.Class<?> getJavaClass();

	/**
	 * E.g. returns <code>Byte.class</code>
	 */
	java.lang.Class<?> getWrapperClass();
	
}
