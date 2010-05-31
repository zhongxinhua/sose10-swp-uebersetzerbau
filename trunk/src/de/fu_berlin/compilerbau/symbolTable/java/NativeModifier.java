package de.fu_berlin.compilerbau.symbolTable.java;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.NATIVE;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.util.Visibility;

/**
 * This class takes a bitmap representation of
 * {@link java.lang.reflect.Modifier modifier of Java} and lets it be read like
 * a {@link Modifier SymbolType modifier}.
 * @author kijewski
 */
final class NativeModifier implements Modifier {

	protected final int modifiers;
	
	public NativeModifier(int modifiers) {
		this.modifiers = modifiers;
	}
	
	@Override
	public boolean isFinal() {
		return (FINAL & modifiers) != 0;
	}
	
	@Override
	public boolean isNative() {
		return (NATIVE & modifiers) != 0;
	}
	
	@Override
	public boolean isStatic() {
		return (STATIC & modifiers) != 0;
	}
	
	@Override
	public Visibility visibility() {
		if((PUBLIC & modifiers) != 0) {
			return Visibility.PUBLIC;
		} else if((PROTECTED & modifiers) != 0) {
			return Visibility.PROTECTED;
		} else if((PRIVATE & modifiers) != 0) {
			return Visibility.PRIVATE;
		} else {
			return Visibility.DEFAULT;
		}
	}
	
}
