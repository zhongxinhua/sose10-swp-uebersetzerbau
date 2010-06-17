package de.fu_berlin.compilerbau.parser;

import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;

public class ForEachStatement extends Statement {
	Expression element;
	Expression values;
	
	public ForEachStatement(DomNode node) {
		//<foreach element="abc"  values="xyz" /> 
		if(!node.getName().equals("foreach")) {
			ErrorHandler.error(node, "this is not a foreach statement!");
			return;
		}
		
		if(node.hasAttribute("element")) {
			PositionString elem = node.getAttribute("element");
			if(elem.length()>0) {
				element = Expression.build(elem, ExpressionType.RVALUE);
				if(!(element instanceof Identifier))
					ErrorHandler.error(elem, "attribute must be a simple identifier");
			} else
				ErrorHandler.error(elem, "invalid value for attribute");
		} else
			ErrorHandler.error(node, "missing element-attribute");
		
		if(node.hasAttribute("values")) {
			PositionString vals = node.getAttribute("element");
			if(vals.length()>0) {
				values = Expression.build(vals, ExpressionType.RVALUE);
				//TODO check if values is an array
			} else
				ErrorHandler.error(vals, "invalid value for attribute");
		} else
			ErrorHandler.error(node, "missing values-attribute");
	}
}
