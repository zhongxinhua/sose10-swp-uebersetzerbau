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

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Decodes entity references "on the fly".
 * @param <BackingBuffer> Type of the buffer to append read characters into.
 * @author rene
 */
public class XmlDecoder<BackingBuffer extends Appendable> implements Appendable, Serializable {

	private static final long serialVersionUID = -8906550416366182580L;
	
	/**
	 * Entity map to use.
	 */
	protected final Map<String,Character> entityMap;
	
	/**
	 * Buffer to write into.
	 */
	protected final BackingBuffer backingBuffer;
	
	/**
	 * Characters between the ampersand and the semicolon.
	 * @see entityCharacterAt
	 */
	protected final char[] entityCharacters = new char[16];
	
	/**
	 * Number of characters between the ampersand and the semicolon (inclusively).
	 * @see #entityCharacter
	 */
	protected int entityCharacterAt = 0;
	
	/**
	 * If true, invalid characters will be pushed.
	 * If false, an exception will be thrown.
	 */
	protected boolean panicReactionIsRecovery = true;
	
	private static final Map<String,Character> XML_ENTITY_MAP_ = new HashMap<String,Character>();
	static {
		XML_ENTITY_MAP_.put("&lt", '<');
		XML_ENTITY_MAP_.put("&gt", '>');
		XML_ENTITY_MAP_.put("&amp", '&');
		XML_ENTITY_MAP_.put("&apos", '\'');
		XML_ENTITY_MAP_.put("&quot", '"');
	}
	
	/**
	 * The default entity map of XML 1.0, Fifth Edition.
	 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#sec-predefined-ent">Extensible Markup Language (XML) 1.0 (Fifth Edition)</a>
	 */
	public static final Map<String,Character> XML_ENTITY_MAP = Collections.unmodifiableMap(XML_ENTITY_MAP_);
	
	public XmlDecoder(BackingBuffer backingBuffer, Map<String,Character> entityMap) {
		this.backingBuffer = backingBuffer;
		this.entityMap = entityMap;
	}
	
	public XmlDecoder(BackingBuffer backingBuffer) {
		this(backingBuffer, XML_ENTITY_MAP_);
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 * @throws IOException if an invalid entity was pushed.
	 */
	@Override
	public Appendable append(CharSequence csq) throws IOException {
		append(csq, 0, csq.length());
		return this;
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 * @throws IOException if an invalid entity was pushed.
	 */
	@Override
	public Appendable append(final char c) throws IOException {
		if(entityCharacterAt == -1) {
			if(c != '&') {
				backingBuffer.append(c);
			} else {
				entityCharacters[0] = '&';
				entityCharacterAt = 1;
			}
		} else {
			if(c == ';') {
				final String ref = getAndPurgeEntityReference();
				final Character entity = entityMap.get(ref);
				if(entity == null) {
					doPanicReaction(ref);
					append(c);
				} else {
					backingBuffer.append(entity.charValue());
				}
			} else if(entityCharacterAt < entityCharacters.length && c != '&') {
				entityCharacters[entityCharacterAt ++] = c;
			} else { // buffer exhausted or another ampersand hit
				doPanicReaction();
				append(c);
			}
		}
		return this;

	}

	/**
	 * {@inheritDoc}
	 * @throws IOException {@inheritDoc}
	 * @throws IOException if an invalid entity was pushed.
	 */
	@Override
	public Appendable append(CharSequence csq, int start, int end) throws IOException {
		for(int i = start; i < end; ++i) {
			append(csq.charAt(i));
		}
		return this;
	}

	/**
	 * Not to have any trailing data you have to call finish()!
	 * @throws IOException if an invalid entity was pushed.
	 */
	public void finish() throws IOException {
		if(entityCharacterAt >= 0) {
			doPanicReaction();
		}
	}
	
	protected String getAndPurgeEntityReference() {
		final String result = String.valueOf(entityCharacters, 0, entityCharacterAt);
		entityCharacterAt = 0;
		return result;
	}

	protected void doPanicReaction(String string) throws IOException {
		if(!panicReactionIsRecovery) {
			new IOException("Invalid or unfinished entity: " + string);
		}
		backingBuffer.append(string);
	}

	protected void doPanicReaction() throws IOException {
		doPanicReaction(getAndPurgeEntityReference());
	}
	
	@Override
	public String toString() {
		if(panicReactionIsRecovery) {
			return backingBuffer.toString() + String.valueOf(entityCharacters, 0, entityCharacterAt);
		} else {
			return backingBuffer.toString();
		}
	}

}
