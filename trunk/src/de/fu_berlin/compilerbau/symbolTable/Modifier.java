package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.Visibility;

public interface Modifier {
	
	Visibility visibility();
	boolean isStatic();
	boolean isFinal();
	boolean isNative();
	
}
