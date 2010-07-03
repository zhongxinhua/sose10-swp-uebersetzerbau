/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
 *                      Markus Rudolf  (<surname>@mi.fu-berlin.de)
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

public class PositionBean implements StreamPosition {
	
	public static PositionBean ONE_ONE_ONE = new PositionBean(1,1,1);
	public static PositionBean ZERO = new PositionBean(0,0,0);
	
	private static final long serialVersionUID = 3886058529055495237L;
	
	protected int start, line, character;
	
	public PositionBean(int start, int line, int character) {
		this.start = start;
		this.line = line;
		this.character = character;
	}
	
	public PositionBean(StreamPosition pos) {
		this.start = pos.getStart();
		this.line = pos.getLine();
		this.character = pos.getColumn();
	}
	
	@Override
	public int getColumn() {
		return character;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	@Override
	public int getStart() {
		return start;
	}
	
	@Override
	public String toString() {
		return "[" + line + ":" + character + "=@" + start + "]";
	}
	
}
