/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.symbolTable;

import de.fu_berlin.compilerbau.util.Visibility;

/**
 * Returns singletons for the various combinations of {@link Modifier modifiers}.
 * @see Modifier
 * @author rene
 */
public final class GetModifier {
	
	private GetModifier() {
		// void
	}
	
	private static Modifier[] modifiers = new Modifier[1 << 5];
	
	public static Modifier getModifier(final Visibility visibility, final boolean isStatic,
			final boolean isFinal, final boolean isNative) {
		
		int position = 0;
		position += visibility.ordinal();
		position <<= 1;
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
