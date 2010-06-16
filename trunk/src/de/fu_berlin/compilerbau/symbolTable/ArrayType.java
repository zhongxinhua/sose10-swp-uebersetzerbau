package de.fu_berlin.compilerbau.symbolTable;

public interface ArrayType extends Class {

	Symbol getComponentType();
	int getDimension();
	
}
