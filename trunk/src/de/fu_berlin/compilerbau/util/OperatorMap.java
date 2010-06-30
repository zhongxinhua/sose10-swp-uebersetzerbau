package de.fu_berlin.compilerbau.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

final class OperatorTriple implements Comparable<OperatorTriple> {
	
	public final Class<?> left, right;
	public final String operator;
	
	public OperatorTriple(Class<?> left, Class<?> right, String operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	public static final Comparator<OperatorTriple> COMPARATOR = new Comparator<OperatorTriple>() {

		/**
		 * @see Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(OperatorTriple left, OperatorTriple right) {
			
			final int loh = left.operator.hashCode();
			final int roh = right.operator.hashCode();
			if(loh != roh) {
				return loh > roh ? +1 : -1;
			}
			
			final int llh = left.left.getCanonicalName().hashCode();
			final int rlh = right.left.getCanonicalName().hashCode();
			if(llh != rlh) {
				return llh > rlh ? +1 : -1;
			}
			
			final int lrh = left.right.getCanonicalName().hashCode();
			final int rrh = right.right.getCanonicalName().hashCode();
			if(lrh != rrh) {
				return lrh > rrh ? +1 : -1;
			}
			
			return 0;
		}
		
	};

	@Override
	public int compareTo(OperatorTriple right) {
		return COMPARATOR.compare(this, right);
	}
	
}

final public class OperatorMap {

	protected static final Class<?>[] TYPES = new Class<?>[] { boolean.class, byte.class, char.class,
			double.class, float.class, int.class, long.class, short.class };
	protected static final Class<?>[] BITABLE_TYPES = new Class<?>[] { boolean.class, byte.class, char.class,
		int.class, long.class, short.class };
	protected static final Class<?>[] NUMBERS = new Class<?>[] { byte.class, double.class, float.class,
			int.class, long.class, short.class };
	
	protected static final String[] COMPARATIONS = new String[] {"<", "<=", "==", "!=", ">=", ">" };
	protected static final String[] ARITHMETIC_OPS = new String[] { "*", "/", "%", "+", "-" };
	protected static final String[] BIT_OPS = new String[] { "&", "|", "^" };
	protected static final String[] LOGICAL_OPS = new String[] { "&&", "||" };
	
	protected static Map<OperatorTriple, Class<?>> OPERATIONS =
			new TreeMap<OperatorTriple, Class<?>>(OperatorTriple.COMPARATOR);
	
	static {
		// comma operation: A × B → B
		for(final Class<?> left : TYPES) {
			for(final Class<?> right : TYPES) {
				final OperatorTriple operatorTriple = new OperatorTriple(left, right, ",");
				OPERATIONS.put(operatorTriple, right);
			}
		}
		
		// logical operations: bool × bool → bool
		for(final String logicalOp : LOGICAL_OPS) {
			final OperatorTriple operatorTriple = new OperatorTriple(boolean.class, boolean.class, logicalOp);
			OPERATIONS.put(operatorTriple, boolean.class);
		}
		
		// comparations between numbers: A × B → bool
		for(final Class<?> left : NUMBERS) {
			for(final Class<?> right : NUMBERS) {
				for(final String comparator : COMPARATIONS) {
					final OperatorTriple operatorTriple = new OperatorTriple(left, right, comparator);
					OPERATIONS.put(operatorTriple, boolean.class);
				}
			}
		}
		
		// equalizations: A × A → bool
		for(final Class<?> type : TYPES) {
			final OperatorTriple operatorTriple1 = new OperatorTriple(type, type, "==");
			OPERATIONS.put(operatorTriple1, boolean.class);
			
			final OperatorTriple operatorTriple2 = new OperatorTriple(type, type, "!=");
			OPERATIONS.put(operatorTriple2, boolean.class);
		}
		
		// bitwise operations: A × A → A
		for(final Class<?> type : BITABLE_TYPES) {
			for(final String bitOp : BIT_OPS) {
				final OperatorTriple operatorTriple = new OperatorTriple(type, type, bitOp);
				OPERATIONS.put(operatorTriple, type);
			}
		}
		
		// TODO: arith
	}
	
	public static 
	
}
