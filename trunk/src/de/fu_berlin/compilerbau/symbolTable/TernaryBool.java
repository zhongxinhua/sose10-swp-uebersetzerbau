package de.fu_berlin.compilerbau.symbolTable;

public enum TernaryBool {
	
	FALSE {
		@Override
		public String toString() {
			return "false";
		}
	},
	
	MAYBE {
		@Override
		public String toString() {
			return "maybe";
		}
	},
	
	TRUE {
		@Override
		public String toString() {
			return "true";
		}
	};
	
	static TernaryBool and(TernaryBool l, TernaryBool r) {
		if(l == FALSE || r == FALSE) {
			return FALSE;
		} if(l == MAYBE || r == MAYBE) {
			return MAYBE;
		} else {
			return TRUE;
		}
	}
	
	static TernaryBool or(TernaryBool l, TernaryBool r) {
		if(l == TRUE || r == TRUE) {
			return TRUE;
		} if(l == MAYBE || r == MAYBE) {
			return MAYBE;
		} else {
			return FALSE;
		}
	}
	
	static TernaryBool not(TernaryBool b) {
		if(b == TRUE) {
			return FALSE;
		} else if(b == FALSE) {
			return TRUE;
		} else {
			return MAYBE;
		}
	}
	
	static TernaryBool valueOf(boolean b) {
		return b ? TRUE : FALSE;
	}
	
	static TernaryBool valueOf(Boolean b) {
		return b != null ? b ? TRUE : FALSE : MAYBE;
	}
	
}
