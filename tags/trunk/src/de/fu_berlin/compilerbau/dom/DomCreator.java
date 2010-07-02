package de.fu_berlin.compilerbau.dom;

/**
 * @author stefan
 * @author rene
 */

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import de.fu_berlin.compilerbau.dom.impl.DomAttributeImpl;
import de.fu_berlin.compilerbau.dom.impl.DomNodeImpl;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;
import de.fu_berlin.compilerbau.xmlNodeStream.NodeType;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNode;
import de.fu_berlin.compilerbau.xmlNodeStream.XmlNodeStream;
import de.fu_berlin.compilerbau.xmlNodeStream.impl.XmlNodeStreamFactory;

public class DomCreator {
	static private Iterator<XmlNode> _iter;
	static private XmlNode _nextNodeToRead;
	
	protected static final class DomCreatorException extends RuntimeException {
		
		private static final long serialVersionUID = 465965183063591708L;

		DomCreatorException(StreamPosition where, String msg) {
			super("DOM tree could not be created.");
			ErrorHandler.warning(where, msg);
		}
		
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
	
	static private DomNode readChild(boolean piAllowed) {
		// get first Tag
		XmlNode currNode;
		first:for (;;) {
			if (!hasNextNode()) {
				return null;
			}
			currNode = readNode();
			switch (currNode.getType()) {
				case NT_ERROR: throw new DomCreatorException(currNode, "Wrong input data.");
				case NT_TEXT: {
					if (isAllWhiteSpace(currNode.getValue())) {
						 continue; // skip over
					} else {
						throw new DomCreatorException(currNode, "Text data occurred outside of the root node.");
					}
				}
				case NT_COMMENT: continue; // skip over
				case NT_TAG: break first;
				case NT_END_TAG: throw new DomCreatorException(currNode, "Data begins with a closing XML node.");
				case NT_ATTR: throw new DomCreatorException(currNode, "[INTERNAL] The XmlNodeStream in buggy! [NT_ATTR]");
				case NT_PI: {
					if (piAllowed) {
						continue; // skip over
					} else {
						throw new DomCreatorException(currNode, "A processing instruction must not be written inside the root node.");
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
				throw new DomCreatorException(currNode, "Missing </" + rootName + ">");
			}
			currNode = readNode();
			switch (currNode.getType()) {
				case NT_ERROR: throw new DomCreatorException(currNode, "Wrong input data.");
				case NT_TEXT: continue; // skip over
				case NT_COMMENT: continue; // skip over
				case NT_TAG: throw new DomCreatorException(currNode, "Multiple elements in root!");
				case NT_END_TAG: break end;
				case NT_ATTR: throw new DomCreatorException(currNode, "[INTERNAL] The XmlNodeStream in buggy! [NT_ATTR]");
				case NT_PI: throw new DomCreatorException(currNode, "A processing instruction may not occur after the root node.");
				default: throw new DomCreatorException(currNode, "[INTERNAL] The XmlNodeStream in buggy! [" + currNode.getType() + "]");
			}
		}
		
		if (currNode.getKey() != null && !currNode.getKey().equals(rootName)) {
			throw new DomCreatorException(currNode, "Invalid </" + currNode.getKey() + ">");
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
				case NT_ERROR: throw new DomCreatorException(currNode, "Wrong input data.");
				case NT_TEXT: continue; // skip over
				case NT_COMMENT: continue; // skip over
				case NT_TAG: {
					unreadNode(currNode);
					DomNode child = readChild(false);
					if (child == null) {
						throw new IllegalStateException("child == null"); // cannot happen
					}
					children.add(child);
					break;
				}
				case NT_END_TAG: {
					unreadNode(currNode);
					return children;
				}
				case NT_ATTR: throw new DomCreatorException(currNode, "[INTERNAL] The XmlNodeStream in buggy! [NT_ATTR]");
				case NT_PI: throw new DomCreatorException(currNode, "A processing instruction may not occur after inside a node.");
				default: throw new DomCreatorException(currNode, "[INTERNAL] The XmlNodeStream in buggy! [" + currNode.getType() + "]");
			}
		}

		throw new DomCreatorException(PositionBean.ZERO, "Missing </" + elem.getName() + "> at end of file");
	}
}
