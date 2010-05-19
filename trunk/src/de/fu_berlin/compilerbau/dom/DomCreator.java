package de.fu_berlin.compilerbau.dom;

/**
 * @author stefan
 */

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.dom.impl.DomAttributeImpl;
import de.fu_berlin.compilerbau.dom.impl.DomNodeImpl;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;
import de.fu_berlin.compilerbau.xmlNodeStream.impl.XmlNodeStreamFactory;


public class DomCreator {
	static private Iterator<XmlNode> _iter;
	
	static private XmlNode _nextNodeToRead;
	static private void unreadNode(XmlNode node) throws IllegalStateException {
		if(_nextNodeToRead == null) {
			_nextNodeToRead = node;
		} else {
			throw new IllegalStateException("_nextNodeToRead != null");
		}
	}
	static private boolean hasNextNode() {
		return _nextNodeToRead != null || _iter.hasNext();
	}
	static private XmlNode readNode() throws NoSuchElementException {
		XmlNode result;
		if(_nextNodeToRead == null) {
			result = _iter.next();
		} else {
			result = _nextNodeToRead;
			_nextNodeToRead = null;
		}
		return result;
	}
	
	/**
	 * Use this method to initialize or reset the DomCreator.
	 * 
	 * @param file - the source File
	 * @throws IOException the reader threw an Exception
	 */
	static public void init(Reader file) throws IOException {
		XmlNodeStream stax = XmlNodeStreamFactory.createNewInstance(file);
		_iter = stax.iterator();
	}
	
	static private DomNode readChild(boolean piAllowed) {
		// get first Tag
		XmlNode currNode;
		first:for (;;) {
			if (!hasNextNode()) {
				return null;
			}
			currNode = readNode();
			switch (currNode.getType()) {
				case NT_ERROR: throw new RuntimeException(currNode.toString()); // TODO: proper type
				case NT_TEXT: {
					if (isAllWhiteSpace(currNode.getValue())) {
						 continue; // skip over
					} else {
						throw new RuntimeException(currNode.toString()); // TODO: proper type
					}
				}
				case NT_COMMENT: continue; // skip over
				case NT_TAG: break first;
				case NT_END_TAG: throw new RuntimeException(currNode.toString()); // TODO: proper type
				case NT_ATTR: throw new IllegalStateException(currNode.toString()); // cannot happen
				case NT_PI: {
					if (piAllowed) {
						continue; // skip over
					} else {
						throw new RuntimeException(currNode.toString()); // TODO: proper type
					}
				}
				default: throw new IllegalStateException(currNode.toString()); // there isn't another case 
			}
		}

		DomNodeImpl root = new DomNodeImpl();
		final PositionString rootName = currNode.getKey();
		root.setName(rootName);
		
		final LinkedList<DomAttribute> rootAttr = getAttributes();
		final LinkedList<DomNode> rootChilds = getChildrenForElement(root);
		
		end:for (;;) {
			if (!hasNextNode()) {
				throw new RuntimeException("Missing </" + rootName + ">"); // TODO: proper type
			}
			currNode = readNode();
			switch (currNode.getType()) {
				case NT_ERROR: throw new RuntimeException(currNode.toString()); // TODO: proper type
				case NT_TEXT: continue; // skip over
				case NT_COMMENT: continue; // skip over
				case NT_TAG: throw new RuntimeException("Multiple elements in root!"); // TODO: proper type
				case NT_END_TAG: break end;
				case NT_ATTR: throw new IllegalStateException(currNode.toString()); // cannot happen
				case NT_PI: throw new RuntimeException(currNode.toString()); // TODO: proper type
				default: throw new IllegalStateException(currNode.toString()); // there isn't another case 
			}
		}
		
		if (currNode.getKey() != null && !currNode.getKey().equals(rootName)) {
			throw new RuntimeException("Invalid </" + currNode.getKey() + ">"); // TODO: proper type
		}

		root.addChilds(rootChilds);
		root.addAttributes(rootAttr);
		return root;
	}

	private static boolean isAllWhiteSpace(CharSequence string) {
		for (int i = 0; i < string.length(); ++i) {
			if (!Character.isWhitespace(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Create the whole DOM-Tree if the XML Structure is valid.
	 * Otherwise the compiler ends with a short error Report.
	 * <p>
	 * Caution: The DomCreator have to be initialized first.
	 * 
	 * @return DOM-Tree or null, if input was empty
	 */
	static public DomNode createDOM() {
		return readChild(true);
	}

	/**
	 * @return Attributelist for the current Element
	 */
	static private LinkedList<DomAttribute> getAttributes() {
		LinkedList<DomAttribute> attrList = new LinkedList<DomAttribute>();
		
		// we want all attributes
		while (hasNextNode()) {
			XmlNode currNode = readNode();
			if (currNode.getType() == NodeType.NT_ATTR) {
				DomAttributeImpl attr = new DomAttributeImpl();
				attr.setName(currNode.getKey());
				attr.setValue(currNode.getValue());
				attrList.add(attr);
			} else {
				unreadNode(currNode);
				break;
			}
		}
		
		return attrList;
	}

	/**
	 * @param elem - parent for the childs
	 * @return Childlist for the Element elem
	 */
	static private LinkedList<DomNode> getChildrenForElement(DomNodeImpl elem) {
		LinkedList<DomNode> children = new LinkedList<DomNode>();
		
		while (hasNextNode()) {
			XmlNode currNode = readNode();
			switch (currNode.getType()) {
				case NT_ERROR: throw new RuntimeException(currNode.toString()); // TODO: proper type
				case NT_TEXT: continue; // skip over
				case NT_COMMENT: continue; // skip over
				case NT_TAG: {
					unreadNode(currNode);
					DomNode child = readChild(false);
					if (child == null) {
						throw new IllegalStateException("child == null"); // cannot happen
					}
					children.push(child);
					break;
				}
				case NT_END_TAG: {
					unreadNode(currNode);
					return children;
				}
				case NT_ATTR: throw new IllegalStateException(currNode.toString()); // cannot happen
				case NT_PI: throw new RuntimeException(currNode.toString()); // TODO: proper type
				default: throw new IllegalStateException(currNode.toString()); // there isn't another case 
			}
		}
		
		throw new RuntimeException("Missing </" + elem.getName() + ">"); // TODO: proper type
	}
}
