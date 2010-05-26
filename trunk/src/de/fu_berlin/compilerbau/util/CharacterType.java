package de.fu_berlin.compilerbau.util;

public final class CharacterType {
	
	private CharacterType() {
		// void
	}
	
	public static boolean isWhitespace(char ch) {
		return Character.isWhitespace(ch);
	}
	
	public static boolean isDigit(char ch) {
		return Character.isDigit(ch);
	}
	
	public static boolean isValidFirstLetterForIdentifier(char ch) {
		return ch=='$' || ch=='_' || Character.isLetter(ch);
	}
	
	public static boolean isValidSecondLetterForIdentifier(char ch) {
		return ch=='$' || ch=='_' || Character.isLetterOrDigit(ch);
	}

	public static boolean isValidFirstLetterForTag(char ch) {
		return ch=='_' || Character.isLetter(ch);
	}
	
	public static boolean isValidSecondLetterForTag(char ch) {
		return ch=='_' || ch==':' || ch=='-' || ch=='.' || Character.isLetterOrDigit(ch);
	}
	
}
