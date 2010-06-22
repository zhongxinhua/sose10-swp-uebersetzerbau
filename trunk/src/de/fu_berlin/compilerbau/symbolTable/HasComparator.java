package de.fu_berlin.compilerbau.symbolTable;

import java.util.Comparator;

public interface HasComparator<E> {
	
	@InternalMethod
	Comparator<? super E> comparator();
	
}
