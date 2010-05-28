package de.fu_berlin.compilerbau.symbolTable.impl;

import static de.fu_berlin.compilerbau.symbolTable.impl.CONSTANTS.WRONG_MAP_METHOD;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;

class IdentitySet<E> implements Set<Entry<E, E>> {
	
	final Iterable<E> backing;
	
	IdentitySet(Iterable<E> backing) {
		this.backing = backing;
	}

	@Override
	public boolean add(Entry<E, E> e) {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public boolean addAll(Collection<? extends Entry<E,E>> c) {
		boolean result = false;
		for(Entry<E, E> e : c) {
			if(add(e)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public boolean contains(Object o) {
		if(o == null) {
			throw new NullPointerException();
		}
		for(E e : backing) {
			if(o.equals(e)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return backing.iterator().hasNext();
	}

	@Override
	public Iterator<Entry<E, E>> iterator() {
		final Iterator<E> iterator = backing.iterator();
		return new Iterator<Entry<E, E>>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Entry<E, E> next() {
				final E next = iterator.next();
				return new Entry<E, E>() {
					@Override
					public E getKey() {
						return next;
					}

					@Override
					public E getValue() {
						return next;
					}

					@Override
					public E setValue(E value) {
						throw new UnsupportedOperationException(WRONG_MAP_METHOD);
					}
				};
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(WRONG_MAP_METHOD);
			}
		};
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(WRONG_MAP_METHOD);
	}

	@Override
	public int size() {
		int i = 0;
		for(@SuppressWarnings("unused") E e : backing) {
			++i;
		}
		return i;
	}

	@Override
	public Object[] toArray() {
		LinkedList<E> result = new LinkedList<E>();
		for(E e : backing) {
			result.add(e);
		}
		return result.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		LinkedList<E> result = new LinkedList<E>();
		for(E e : backing) {
			result.add(e);
		}
		return result.toArray(a);
	}
	
}
