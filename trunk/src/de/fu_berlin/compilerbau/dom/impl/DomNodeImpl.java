package de.fu_berlin.compilerbau.dom.impl;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomAttribute;
import de.fu_berlin.compilerbau.dom.DomNode;


public class DomNodeImpl implements DomNode {

	private LinkedList<DomAttribute> _attributes;
	private LinkedList<DomNode> _childList;
	
	private DomNodeImpl _parent;
	private String _nodeName;
	
	@Override
	public List<DomAttribute> getAttributes() {
		return _attributes;
	}

	@Override
	public List<DomNode> getChilds() {
		return _childList;
	}

	@Override
	public String getName() {
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

	public void setName(String nodeName) {
		_nodeName = nodeName;		
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

}
