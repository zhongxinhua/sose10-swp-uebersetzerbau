package de.fu_berlin.compilerbau.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CombinedIteratable<E> extends Object implements Iterable<E> {
	
	protected final Iterable<E>[] iters;
	
	public CombinedIteratable(Iterable<E>[] iters) {
		this.iters = iters;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			
			private int index = 0;
			private Iterator<E> i;

			@Override
			public boolean hasNext() {
				if(i != null && i.hasNext()) {
					return true;
				}
				while(index >= iters.length) {
					Iterable<E> iter = iters[index ++];
					if(iter != null) {
						i = iters[index ++].iterator();
						if(i.hasNext()) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public E next() {
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				return i.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}

}
