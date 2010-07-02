package de.fu_berlin.compilerbau.dom.impl;

/**
 * @author stefan
 */

import de.fu_berlin.compilerbau.dom.DomAttribute;
import de.fu_berlin.compilerbau.util.PositionString;

public class DomAttributeImpl implements DomAttribute {

	private PositionString _name, _value;
	
	public DomAttributeImpl() {
		_name = null;
		_value = null;
	}
	
	@Override
	public PositionString getName() {
		return _name;
	}

	@Override
	public PositionString getValue() {
		return _value;
	}

	public void setName(PositionString value) {
		_name = value;
	}

	public void setValue(PositionString value) {
		_value = value;
	}
}
