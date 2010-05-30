package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.Visibility;

public final class GetModifier {
	
	private GetModifier() {
		// void
	}
	
	private static Modifier[] modifiers = new Modifier[1 << 5];
	
	public static Modifier getModifier(final Visibility visibility, final boolean isStatic,
			final boolean isFinal, final boolean isNative) {
		
		int position = 0;
		position += visibility.ordinal();
		position <<= 2;
		position += isStatic ? 1 : 0;
		position <<= 1;
		position += isFinal ? 1 : 0;
		position <<= 1;
		position += isNative ? 1 : 0;
		
		if(modifiers[position] == null) {
			modifiers[position] = new Modifier() {
				@Override
				public Visibility visibility() {
					return visibility;
				}
				
				@Override
				public boolean isStatic() {
					return isStatic;
				}
				
				@Override
				public boolean isNative() {
					return isNative;
				}
				
				@Override
				public boolean isFinal() {
					return isFinal;
				}
			};
		}
		return modifiers[position];
		
	}
	
}
