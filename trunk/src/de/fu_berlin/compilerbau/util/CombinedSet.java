package de.fu_berlin.compilerbau.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

class CombinedSet<E extends Comparable<E>> implements Set<E> {
	
	protected final Set<E>[] sets;
	
	public CombinedSet(Set<E>[] sets) {
		this.sets = sets;
	}

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
	public boolean contains(Object o) {
		for(Set<E> s : sets) {
			if(!s.contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Set<E> s : sets) {
			if(!s.containsAll(c)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for(Set<E> s : sets) {
			if(!s.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		return new CombinedIteratable<E>(sets).iterator();
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

	protected int SIZE = -1;
	@Override
	public int size() {
		if(SIZE < 0) {
			int v = 0;
			for(Set<E> s : sets) {
				v += s.size();
			}
			SIZE = v;
		}
		return SIZE;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for(E e : this) {
			result[i ++] = e;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		final Class<?> componentType = a.getClass().getComponentType();
		final int len = size();
		if(len > a.length) {
			a = (T[]) Array.newInstance(componentType, len);
		}
		final Iterator<E> iter = iterator();
		for(int i = 0; i < len; ++i) {
			a[i] = (T) componentType.cast(iter.next());
		}
		if(len < a.length+1) {
			a[len] = null;
		}
		return a;
	}

}
