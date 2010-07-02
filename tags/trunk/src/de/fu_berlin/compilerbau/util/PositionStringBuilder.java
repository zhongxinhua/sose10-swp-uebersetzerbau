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

public class PositionStringBuilder extends PositionBean
		implements Appendable, StreamPosition {

	private static final long serialVersionUID = -5035361425725441705L;
	
	public PositionStringBuilder(int start, int line, int character) {
		super(start, line, character);
	}
	
	public PositionStringBuilder(StreamPosition pos) {
		super(pos);
	}
	
	protected final StringBuilder builder = new StringBuilder();

	@Override
	public Appendable append(CharSequence sq) {
		builder.append(sq);
		return this;
	}

	@Override
	public Appendable append(char c) {
		builder.append(c);
		return this;
	}

	@Override
	public Appendable append(CharSequence sq, int start, int end) {
		builder.append(sq, start, end);
		return this;
	}
	
	public PositionString toPositionString() {
		return new PositionString(builder.toString(), this);
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

}
