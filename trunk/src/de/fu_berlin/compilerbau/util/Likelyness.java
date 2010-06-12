package de.fu_berlin.compilerbau.util;

public enum Likelyness {
	
	IMPOSSIBLE,
	MAYBE,
	YES;
	
	public Likelyness or(Likelyness right) {
		return compareTo(right) >= 0 ? this : right;
	}
	
	public Likelyness and(Likelyness right) {
		return compareTo(right) <= 0 ? this : right;
	}
	
}
