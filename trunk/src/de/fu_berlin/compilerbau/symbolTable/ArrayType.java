package de.fu_berlin.compilerbau.symbolTable;

/**
 * This is a type with positive dimension &ge; 1.
 * @author rene
 */
public interface ArrayType extends Class {

	/**
	 * E.g. returns the <code>A</code> in <code>A[]</code>.
	 */
	Symbol getComponentType();
	
	/**
	 * @return value &ge; 1
	 */
	int getDimension();
	
}
