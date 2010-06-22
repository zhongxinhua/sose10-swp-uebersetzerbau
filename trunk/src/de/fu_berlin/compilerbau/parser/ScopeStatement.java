package de.fu_berlin.compilerbau.parser;

import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * <b>Description</b><br>{@link ScopeStatement} is a subclass of {@link Statement} representing a
 * &ltscope/&gt statement forming a node in the parse tree.<br>
 * 
 * <p/>
 * <b>Specification</b><br>
 * The &ltscope/&gt statement <b>has no</b> attribute.
 * <p/>
 * The &ltscope/&gt statement <b>has no</b> optional attributes.
 * <p/>
 * The &ltscope/&gt statement body has:
 * <ul>
 * <li><b>arbitrary</b> statements</li>
 * </ul>
 * @see {@link Statement}
 * @author Sam
 * 
 */
public class ScopeStatement extends Statement {
	private List<Statement> body;

	public ScopeStatement(DomNode node) {
		//check for empty attribute list
		if(!node.getAttributes().isEmpty()){
			ErrorHandler.error(node, this.getClass().toString()+" attributes forbidden!");
		}
		// process child nodes
		for (DomNode child : node.getChilds()) {
			body.add(Statement.build(child));
		}

	}
	
	public List<Statement> getBody() {
		return body;		
	} 

}