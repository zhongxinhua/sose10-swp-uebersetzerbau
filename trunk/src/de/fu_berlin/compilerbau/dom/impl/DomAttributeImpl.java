package de.fu_berlin.compilerbau.dom.impl;

/**
 * @author stefan
 */

import de.fu_berlin.compilerbau.dom.DomAttribute;

public class DomAttributeImpl implements DomAttribute {

	private String _name;
	private String _value;
	
	public DomAttributeImpl() {
		_name = "";
		_value = "";
	}
	
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getValue() {
		return _value;
	}

	public void setName(String value) {
		_name = value;
	}

	public void setValue(String value) {
		_value = value;
	}
}
