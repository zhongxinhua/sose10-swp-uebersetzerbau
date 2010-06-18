package de.fu_berlin.compilerbau.symbolTable;

public interface ArrayType extends Class {

	/**
	 * E.g. returns the <code>A</code> in <code>A[]</code>.
	 */
	Symbol getComponentType();
	
	int getDimension();
	
}
