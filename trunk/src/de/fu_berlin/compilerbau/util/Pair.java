package de.fu_berlin.compilerbau.util;

import java.io.Serializable;

/**
 * @author kijewski
 * @param <L> left side type
 * @param <R> right side type
 */
public class Pair<L, R> implements Serializable {
	
	private static final long serialVersionUID = 2753001835538398591L;
	
	protected L l;
	protected R r;
	
	public Pair(L l, R r) {
		this.l = l;
		this.r = r;
	}
	
	public L getLeft() {
		return l;
	}
	
	public R getRight() {
		return r;
	}
	
}
