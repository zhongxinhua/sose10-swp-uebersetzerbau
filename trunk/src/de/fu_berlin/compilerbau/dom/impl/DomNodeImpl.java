package de.fu_berlin.compilerbau.dom.impl;

/**
 * @author stefan
 */

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomAttribute;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.PositionString;

public class DomNodeImpl implements DomNode {

	private LinkedList<DomAttribute> _attributes;
	private LinkedList<DomNode> _childList;
	
	private DomNodeImpl _parent;
	private PositionString _nodeName;
	
	public DomNodeImpl() {
		_attributes = new LinkedList<DomAttribute>();
		_childList = new LinkedList<DomNode>();
		_parent = null;
		_nodeName = null;
	}
	
	@Override
	public List<DomAttribute> getAttributes() {
		return _attributes;
	}

	@Override
	public List<DomNode> getChilds() {
		return _childList;
	}

	@Override
	public PositionString getName() {
		return _nodeName;
	}

	@Override
	public DomNode getParent() {
		return _parent;
	}

	@Override
	public boolean isLeaf() {
		return _childList.isEmpty();
	}

	@Override
	public boolean isNode() {
		return !_childList.isEmpty();
	}

	public void setName(PositionString rootName) {
		_nodeName = rootName;		
	}
	
	public void setParent(DomNodeImpl parent) {
		_parent = parent;		
	}

	public void addAttribute(DomAttributeImpl attr) {
		_attributes.add(attr);
	}
	
	public void addChild(DomNodeImpl child) {
		_childList.add(child);
	}

	public void addChilds(LinkedList<DomNode> childs) {
		_childList.addAll(childs);
	}

	public void addAttributes(LinkedList<DomAttribute> attr) {
		_attributes.addAll(attr);
	}

	@Override
	public PositionString getAttributeValue(String attributeName) {	
		for (DomAttribute attr : _attributes) {
			if (attr.getName().equals(attributeName)) {
				return attr.getValue();
			}
		}
		
		return null;
	}

	@Override
	public boolean hasAttribute(String attributeName) {
		for (DomAttribute attr : _attributes) {
			if (attr.getName().equals(attributeName)) return true;
		}
		return false;
	}

}
