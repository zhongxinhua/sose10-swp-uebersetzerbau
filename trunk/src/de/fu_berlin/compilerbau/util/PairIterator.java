package de.fu_berlin.compilerbau.util;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

public class PairIterator<A, B> implements Iterable<Map.Entry<A, B>> {
	
	protected final Iterator<A> a;
	protected final Iterator<B> b;
	
	class Result implements Entry<A, B> {
		
		A key;
		B value;

		@Override
		public A getKey() {
			return key;
		}

		@Override
		public B getValue() {
			return value;
		}

		@Override
		public B setValue(B value) {
			final B result = value;
			this.value = value;
			return result;
		}
		
	};
	
	public PairIterator(Iterable<A> a, Iterable<B> b) {
		this.a = a.iterator();
		this.b = b.iterator();
	}

	@Override
	public Iterator<Entry<A, B>> iterator() {
		return new Iterator<Entry<A, B>>() {
			
			boolean readA, readB;

			@Override
			public boolean hasNext() {
				return a.hasNext() || b.hasNext();
			}

			@Override
			public Entry<A, B> next() {
				final Result result = new Result();
				if(a.hasNext()) {
					result.key = a.next();
					readA = true;
				} else readA = false;
				if(b.hasNext()) {
					result.value = b.next();
					readB = true;
				} else readB = false;
				if(!readA && !readB) {
					throw new NoSuchElementException();
				}
				return result;
			}

			@Override
			public void remove() {
				if(!readA && !readB) {
					throw new IllegalStateException();
				}
				if(readA) {
					readA = false;
					a.remove();
				}
				if(readB) {
					readB = false;
					b.remove();
				}
			}
			
		};
	}
}
