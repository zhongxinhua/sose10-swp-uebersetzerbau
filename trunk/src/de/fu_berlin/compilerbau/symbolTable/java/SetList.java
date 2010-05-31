package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * That what immutable {@link Set Sets} and {@link List Lists} have in common.
 * @author kijewski
 */
abstract class SetList<E> implements Set<E>, List<E> {
	
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void add(int arg0, E arg1) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public E remove(int arg0) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public E set(int arg0, E arg1) {
		throw new UnsupportedOperationException();
	}
	
}
