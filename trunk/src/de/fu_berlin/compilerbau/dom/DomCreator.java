package de.fu_berlin.compilerbau.dom;

import java.util.Iterator;
import java.util.LinkedList;

import de.fu_berlin.compilerbau.dom.impl.DomAttributeImpl;
import de.fu_berlin.compilerbau.dom.impl.DomNodeImpl;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;

/*
 * TODO: 
 * - Fehlererkennung und Behebung
 * - Pruefen, ob Klasse statisch sein sollte
 */


public class DomCreator {
	private Iterator<XmlNode> iter;
	private XmlNodeStream stax;
	private XmlNode currNode;

	// kann zum Testen genutzt werden
	/*
	public static void main(String args[]) {
		DomCreator dc = new DomCreator();
		dc.init();
		dc.createDOM();
	}
	
	void init() {
		try {
			stax = XmlNodeStreamFactory.createNewInstance(new FileReader("test.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		iter = stax.iterator();
		currNode = null;
	} */

	DomNode createDOM() {
		DomNodeImpl root = new DomNodeImpl();

		// get first Tag
		while (iter.hasNext()
				&& (currNode = iter.next()).getType() != NodeType.NT_TAG) {
		}

		String rootName = currNode.getKey();
		LinkedList<DomAttribute> rootAttr = getAttributes();
		LinkedList<DomNode> rootChilds = getChildsForElement(rootName);
		
		root.setName(rootName);
		root.addChilds(rootChilds);
		root.addAttributes(rootAttr);
		
		return root;

	}

	private LinkedList<DomAttribute> getAttributes() {
		LinkedList<DomAttribute> attrList = new LinkedList<DomAttribute>();
		
		// we want all attributes
		while ((currNode = iter.next()).getType() == NodeType.NT_ATTR) {
			DomAttributeImpl attr = new DomAttributeImpl();
			
			attr.setName(currNode.getKey());
			attr.setValue(currNode.getValue());

			attrList.add(attr);
		}
		return attrList;
	}

	private LinkedList<DomNode> getChildsForElement(String elemName) {
		LinkedList<DomNode> childs = new LinkedList<DomNode>();
		
		if( currNode.getType() == NodeType.NT_END_TAG ) return childs;
		
		// we want all child's
		while ( currNode.getType() != NodeType.NT_END_TAG ) {
			
			// we are only interested in Tags
			if( currNode.getType() == NodeType.NT_TAG) {
				DomNodeImpl child = new DomNodeImpl();
				
				child.setName(currNode.getKey());
				
				LinkedList<DomAttribute> childAttr = getAttributes();
				LinkedList<DomNode> childChilds = getChildsForElement(elemName);
				
				// TODO: little weird
				child.addChilds(childChilds);
				child.addAttributes(childAttr);
				
				childs.add(child);
			}
			currNode = iter.next();
		}
		
		return childs;
	}
}
