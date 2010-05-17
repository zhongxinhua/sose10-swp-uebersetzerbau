package de.fu_berlin.compilerbau.dom;

/**
 * @author stefan
 */

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;

import de.fu_berlin.compilerbau.dom.impl.DomAttributeImpl;
import de.fu_berlin.compilerbau.dom.impl.DomNodeImpl;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;
import de.fu_berlin.compilerbau.xmlNodeStream.impl.XmlNodeStreamFactory;


public class DomCreator {
	static private Iterator<XmlNode> _iter;
	static private XmlNodeStream _stax;
	static private XmlNode _currNode;
	static private LinkedList<String> _validationStack;
	
	/**
	 * Use this method to initialize or reset the DomCreator.
	 * 
	 * @param file - the source File
	 */
	static public void init(Reader file) {
		try {
			_stax = XmlNodeStreamFactory.createNewInstance(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_iter = _stax.iterator();
		_currNode = null;
		_validationStack = new LinkedList<String>();
	} 

	/**
	 * Create the whole DOM-Tree if the XML Structure is valid.
	 * Otherwise the compiler ends with a short error Report.
	 * <p>
	 * Caution: The DomCreator have to be initialized first.
	 * 
	 * @return DOM-Tree
	 */
	static public DomNode createDOM() {
		DomNodeImpl root = new DomNodeImpl();

		// get first Tag
		while (_iter.hasNext()
				&& (_currNode = getNextXmlNode()).getType() != NodeType.NT_TAG) {
		}

		String rootName = _currNode.getKey();
		_validationStack.push(rootName);
		
		LinkedList<DomAttribute> rootAttr = getAttributes();
		LinkedList<DomNode> rootChilds = getChildsForElement(root);
		
		root.setName(rootName);
		root.addChilds(rootChilds);
		root.addAttributes(rootAttr);
		
		return root;
	}

	/**
	 * @return Attributelist for the current Element
	 */
	static private LinkedList<DomAttribute> getAttributes() {
		LinkedList<DomAttribute> attrList = new LinkedList<DomAttribute>();
		
		// we want all attributes
		while ((_currNode = getNextXmlNode()).getType() == NodeType.NT_ATTR) {
			DomAttributeImpl attr = new DomAttributeImpl();
			
			attr.setName(_currNode.getKey());
			attr.setValue(_currNode.getValue());

			attrList.add(attr);
		}
		return attrList;
	}

	/**
	 * @param elem - parent for the childs
	 * @return Childlist for the Element elem
	 */
	static private LinkedList<DomNode> getChildsForElement(DomNodeImpl elem) {
		LinkedList<DomNode> childs = new LinkedList<DomNode>();
		
		if( _currNode.getType() == NodeType.NT_END_TAG ) {
			_validationStack.pop();
			return childs;
		}
		
		// we want all child's
		while ( _currNode.getType() != NodeType.NT_END_TAG ) {
			
			// we are only interested in Tags
			if( _currNode.getType() == NodeType.NT_TAG) {
				DomNodeImpl child = new DomNodeImpl();
				
				String childName = _currNode.getKey();
				
				_validationStack.push(childName);
				child.setName(childName);
				child.setParent(elem);
				/* int charPos = _stax.getCharacter();
				int linePos = _stax.getLine();
				int startPos = _stax.getStart(); */
				
				LinkedList<DomAttribute> childAttr = getAttributes();
				LinkedList<DomNode> childChilds = getChildsForElement(child);
				
				// TODO: little weird
				child.addChilds(childChilds);
				child.addAttributes(childAttr);
				
				childs.add(child);
			}
			_currNode = getNextXmlNode();
		}
		
		if(!_validationStack.pop().equals(_currNode.getKey())) {
			System.err.println("XML Validation Error\n\tThere may unclosed Tags");
			System.exit(-1);
		}
		
		return childs;
	}
	
	static private XmlNode getNextXmlNode() {
		if (!_iter.hasNext()) {
			System.err.println("XML Validation Error\n\tThere is at least one Tag missing");
			System.exit(-1);
		}
			
		return _iter.next();
	}
}
