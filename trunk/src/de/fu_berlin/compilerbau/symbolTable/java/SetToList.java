package de.fu_berlin.compilerbau.symbolTable.java;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Lets you access a set as if it was a list.
 * @author kijewski
 */
class SetToList<E> extends SetList<E> {
	
	protected final Set<E> backingSet;
	
	public SetToList(Set<E> backingSet) {
		this.backingSet = Collections.unmodifiableSet(backingSet);
	}

	@Override
	public boolean contains(Object o) {
		return backingSet.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return backingSet.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return backingSet.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return backingSet.iterator();
	}

	@Override
	public int size() {
		return backingSet.size();
	}

	@Override
	public Object[] toArray() {
		return backingSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return backingSet.toArray(a);
	}

	@Override
	public E get(int index) {
		if(!backingSet.isEmpty()) {
			Iterator<E> iter = backingSet.iterator();
			for(int i = 0; iter.hasNext(); ++i) {
				E next = iter.next();
				if(i == index) {
					return next;
				}
			}
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int indexOf(Object o) {
		Iterator<E> i= backingSet.iterator();
		int index = 0;
		while(i.hasNext()) {
			E next = i.next();
			if((next == null && o == null) || (next != null && next.equals(o))) {
				return index;
			}
			++index;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		final Iterator<E> iterator = backingSet.iterator();
		
		for(int i = 0; i < index; ++i) {
			if(!iterator.hasNext()) {
				throw new IndexOutOfBoundsException();
			}
			iterator.next();
		}
		
		return new ListIterator<E>() {
			protected int i = index;

			@Override
			public void add(E arg0) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}

			@Override
			public E next() {
				++i;
				return iterator.next();
			}

			@Override
			public int nextIndex() {
				return i;
			}

			@Override
			public E previous() {
				throw new NoSuchElementException();
			}

			@Override
			public int previousIndex() {
				return -1;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(E arg0) {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
}
