package de.fu_berlin.compilerbau.parser;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

public class ForEachStatement extends Statement {
	PositionString element;
	Expression values;
	List<Statement> body = new LinkedList<Statement>();	
	
	public ForEachStatement(DomNode node) {
		//<foreach element="abc"  values="xyz" /> 
		if(!node.getName().equals("foreach")) {
			ErrorHandler.error(node, "this is not a foreach statement!");
			return;
		}
		
		//Element
		if(node.hasAttribute("element")) {
			PositionString elem = node.getAttribute("element");
			if(elem.length()>0) {
				element = elem;
			} else
				ErrorHandler.error(elem, "invalid value for attribute");
		} else
			ErrorHandler.error(node, "missing element-attribute");
		
		//Values
		if(node.hasAttribute("values")) {
			PositionString vals = node.getAttribute("element");
			if(vals.length()>0) {
				values = Expression.build(vals, ExpressionType.RVALUE);
				//TODO check if values is an array
			} else
				ErrorHandler.error(vals, "invalid value for attribute");
		} else
			ErrorHandler.error(node, "missing values-attribute");
		
		//Body
		if (node.isNode()) {
			for (DomNode child : node.getChilds()) {
				Statement stmt = Statement.build(child);
				body.add(stmt);
			}
		}
	}
	
	public PositionString getElement() {
		return element;
	}
	
	public Expression getValues() {
		return values;
	}
	
	public List<Statement> getBody(){
		return body;
	}
}
