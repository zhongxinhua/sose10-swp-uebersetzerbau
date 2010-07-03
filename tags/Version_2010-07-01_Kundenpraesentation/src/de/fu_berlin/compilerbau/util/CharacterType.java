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
