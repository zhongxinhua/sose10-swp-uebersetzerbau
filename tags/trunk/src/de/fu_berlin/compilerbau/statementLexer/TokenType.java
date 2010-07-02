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

package de.fu_berlin.compilerbau.statementLexer;

public enum TokenType {
	
	EOF,
	
	ERROR,

	DOT,

	/**
	 *(
	 */
	PAREN_OPEN,

	/**
	 * )
	 */
	PAREN_CLOSE,

	/**
	 * [
	 */
	BRACKET_OPEN,

	/**
	 * ]
	 */
	BRACKET_CLOSE,
	
	/**
	 * {
	 */
	BRACE_OPEN,

	/**
	 * }
	 */
	BRACE_CLOSE,

	/**
	 * ++
	 */
	INCR,

	/**
	 * --
	 */
	DECR,

	/**
	 * !
	 */
	NOT,

	/**
	 * new
	 */
	NEW,

	/**
	 * null
	 */
	NULL,

	/**
	 * *
	 */
	TIMES,

	/**
	 * /
	 */
	DIVIDES,

	/**
	 * %
	 */
	MOD,

	/**
	 * +
	 */
	PLUS,

	/**
	 * -
	 */
	MINUS,

	/**
	 * &lt;=
	 */
	LE,

	/**
	 * &gt;=
	 */
	GE,

	/**
	 * &lt;
	 */
	LT,

	/**
	 * &gt;
	 */
	GT,

	/**
	 * !=
	 */
	NE,

	/**
	 * ==
	 */
	EQ,

	/**
	 * =
	 */
	ASSIGN,

	/**
	 * &amp;
	 */
	BIT_AND,

	/**
	 * ^
	 */
	BIT_XOR,

	/**
	 * |
	 */
	BIT_OR,

	/**
	 * &amp;&amp;
	 */
	AND,

	/**
	 * ||
	 */
	OR,

	/**
	 * ,
	 */
	COMMA,

	/**
	 * An identifier as specified in the Pad.
	 */
	ID,
	
	/**
	 * An integer.
	 */
	INT,
	
	/**
	 * A floating point number.
	 */
	FLOAT,
	
	/**
	 * A string, either included in quotation marks or apostrophes.
	 */
	STRING
	
}
