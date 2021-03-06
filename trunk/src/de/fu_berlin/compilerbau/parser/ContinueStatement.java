package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;
/**
 * <b>Description</b><br>
 * {@link ContinueStatement} is a subclass of {@link Statement} representing a
 * &ltcontinue/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltcontinue/&gt statement <b>has no</b> attributes.
 * <p/>
 * The &ltcontinue/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltcontinue/&gt statement <b>must be</b> a Leaf.
 * 
 * @author Sam
 * @see {@link DoStatement}
 * 
 */

@SuppressWarnings("serial")
public class ContinueStatement extends Statement {

	public ContinueStatement(DomNode node) {
		setPosition(node);
		
		//check for empty attribute list
		if(!node.getAttributes().isEmpty()){
			ErrorHandler.error(node, this.getClass().toString()+" attributes forbidden!");
		}
		//check for body forbidden restriction
		if (!node.isLeaf()) {
			ErrorHandler.error(node, this.getClass().toString()+" body forbidden!");
		}
	}
	
	@Override
	public String toString() {
		return "continue;";
	}
}