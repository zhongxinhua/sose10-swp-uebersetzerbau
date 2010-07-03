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

package de.fu_berlin.compilerbau.statementLexer.impl;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.TokenType;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class StatementNodeImpl extends PositionBean implements StatementNode {

	private static final long serialVersionUID = -3350158359867002247L;
	protected final TokenType type;
	protected final Object value;
	
	StatementNodeImpl(int start, int line, int character, TokenType type, Object value) {
		super(start, line, character);
		this.type = type;
		this.value = value;
	}
	
	StatementNodeImpl(StreamPosition pos, TokenType type, Object value) {
		super(pos);
		this.type = type;
		this.value = value;
	}

	@Override
	public TokenType getType() {
		return type;
	}

	@Override
	public Object getValue() throws IllegalAccessException {
		if(TokenType.ID.equals(type) || TokenType.STRING.equals(type) ||
				TokenType.INT.equals(type) || TokenType.FLOAT.equals(type)) {
			return value;
		}
		throw new IllegalAccessException();
	}

	@Override
	public CharSequence getString() throws IllegalAccessException {
		if(TokenType.ID.equals(type) || TokenType.STRING.equals(type)) {
			return (CharSequence)value;
		}
		throw new IllegalAccessException();
	}

	@Override
	public Number getNumber() throws IllegalAccessException {
		if(TokenType.INT.equals(type) || TokenType.FLOAT.equals(type)) {
			return (Number)value;
		}
		throw new IllegalAccessException();
	}
	
	@Override
	public String toString() {
		return "[" + type + "=" + value + "]@" + super.toString();
	}

}
