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

import java.io.Serializable;
import java.util.Map;

/**
 * @author kijewski
 * @param <L> left side type
 * @param <R> right side type
 */
public class Pair<L, R> implements Serializable, Map.Entry<L,R> {
	
	private static final long serialVersionUID = 2753001835538398591L;
	
	protected L l;
	protected R r;
	
	public Pair(L l, R r) {
		this.l = l;
		this.r = r;
	}
	
	public L getLeft() {
		return l;
	}
	
	public R getRight() {
		return r;
	}

	@Override
	public L getKey() {
		return l;
	}

	@Override
	public R getValue() {
		return r;
	}

	@Override
	public R setValue(R arg0) {
		throw new UnsupportedOperationException();
	}
	
}
