package de.fu_berlin.compilerbau.symbolTable;

/**
 * This is boolean, byte, ...
 * 
 * <p/>Bear in mind: {@link Void} inherits PrimitiveType but is not a type at all!
 * @author rene
 */
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
