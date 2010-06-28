package de.fu_berlin.compilerbau.annotator;

import java.util.Iterator;
import java.util.List;

import de.fu_berlin.compilerbau.parser.expressions.ArrayAccess;
import de.fu_berlin.compilerbau.parser.expressions.ArrayCreation;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.FloatLiteral;
import de.fu_berlin.compilerbau.parser.expressions.FunctionCall;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.parser.expressions.IntegerLiteral;
import de.fu_berlin.compilerbau.parser.expressions.Literal;
import de.fu_berlin.compilerbau.parser.expressions.MemberAccess;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.ObjectCreation;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.StreamPosition;

public class ExpressionAnnotator {
	private de.fu_berlin.compilerbau.symbolTable.Runtime runtime;
	
	public ExpressionAnnotator(de.fu_berlin.compilerbau.symbolTable.Runtime runtime) {
		this.runtime = runtime;
	}
	
	/**
	 * annotates an expression
	 * @param container
	 * @param expression
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateExpression(SymbolContainer container, Expression expression) throws InvalidIdentifierException {
		if(expression instanceof ArrayAccess)
			return annotateArrayAccess(container, (ArrayAccess) expression);
		else if(expression instanceof ArrayCreation)
			return annotateArrayCreation(container, (ArrayCreation) expression);
		else if(expression instanceof BinaryOperation)
			return annotateBinaryOperation(container, (BinaryOperation) expression);
		else if(expression instanceof Literal) 
			return annotateLiteral(container, (Literal) expression);
		else if(expression instanceof FunctionCall)
			return annotateFunctionCall(container, (FunctionCall) expression);
		else if(expression instanceof Identifier)
			return annotateIdentifier(container, (Identifier) expression);
		else if(expression instanceof MemberAccess)
			return annotateMemberAccess(container, (MemberAccess) expression);
		else if(expression instanceof UnaryOperation)
			return annotateUnaryOperation(container, (UnaryOperation) expression);
		throw new RuntimeException("will never be reached... cookies"+expression);
	}
	
	protected Symbol annotateArrayAccess(SymbolContainer container, ArrayAccess access) throws InvalidIdentifierException {
		//add mention
		Symbol symbol = container.getQualifiedSymbol(access.getName());
		assert(symbol.hasType(SymbolType.VARIABLE));
		Variable symVariable = (Variable) symbol;
		assert(symVariable.getVariableType().hasType(SymbolType.ARRAY_TYPE));
		container.addMention(symbol, access);
		
		//check indices
		for(Expression index : access.getIndices()) {
			Symbol symIndex = annotateExpression(container, index);
		}
		
		return symbol;
	}
	
	protected Symbol annotateArrayCreation(SymbolContainer container, ArrayCreation creation) throws InvalidIdentifierException {
		Symbol arrayType = null;
		for(Expression expression : creation.getElements()) {
			Symbol type = annotateExpression(container, expression);
			if(arrayType == null) {
				arrayType = type;
				continue;
			}

			//TODO
			//if(type.compareTo(o))
		}
		return arrayType;
	}
	
	
	protected Symbol annotateBinaryOperation(SymbolContainer container, BinaryOperation operation) {
		
		return null;
		//TODO
	}
	
	/**
	 * annotates a literal with its type
	 * @param container
	 * @param literal
	 * @return the type of the literal
	 */
	protected Symbol annotateLiteral(SymbolContainer container, Literal literal) {
		Symbol symbol = null;
		if(literal instanceof NullLiteral)
			symbol = runtime.getVoid();
		else if(literal instanceof IntegerLiteral)
			symbol = runtime.getPrimitiveType(int.class);
		else if(literal instanceof FloatLiteral)
			symbol = runtime.getPrimitiveType(float.class);
		else if(literal instanceof StringLiteral)
			symbol = runtime.getPrimitiveType(String.class);
		literal.setSymbol(symbol);
		return symbol;
	}

	/**
	 * annotates a function call
	 * @param container
	 * @param call
	 * @return the return type of the function
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateFunctionCall(SymbolContainer container, FunctionCall call) throws InvalidIdentifierException {
		//check symbol
		final PositionString name = call.getName();
		Symbol symbol = container.getQualifiedSymbol(name);		
		assert(symbol.hasType(SymbolType.METHOD));
		Method symFunction = (Method) symbol;
		
		//check if contructor
		if(call instanceof ObjectCreation)
			assert(symbol.hasType(SymbolType.CONSTRUCTOR));
		
		//check arguments count
		final int neededParamCount = symFunction.getParameters().size();
		final int actualParamCount = call.getArguments().size();
		if(neededParamCount != actualParamCount) {
			ErrorHandler.error(call, "function \""+name+"\" needs "+neededParamCount+" arguments (not "+actualParamCount+")");
			return symFunction.getReturnType();
		}
		
		//check arguments types
		Iterator<Variable> itSyms = symFunction.getParameters().iterator();
		Iterator<Expression> itExpr = call.getArguments().iterator();
		while(itSyms.hasNext()) {
			Variable symArg    = itSyms.next();
			Expression exprArg = itExpr.next();
			Symbol neededType = symArg.getVariableType();
			Symbol actualType = annotateExpression(container, exprArg);
			checkType(exprArg, neededType, actualType);
		}
		return symFunction.getReturnType();
	}
	
	protected void checkType(StreamPosition position, Symbol neededType, Symbol actualType) {
		ErrorHandler.debugMsg(position, "checking type (needed type: "+neededType+"; actual type: "+actualType+")");
		//TODO
	}
	
	/**
	 * annotates an identifier
	 * @param container
	 * @param identifier
	 * @return the type of the identifier
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateIdentifier(SymbolContainer container, Identifier identifier) throws InvalidIdentifierException {
		QualifiedSymbol symbol = container.getQualifiedSymbol(identifier.getName());
		assert(symbol.hasType(SymbolType.VARIABLE));
		identifier.setSymbol(symbol);
		container.addMention(symbol, identifier);
		return ((Variable) symbol).getVariableType();
	}
	
	/**
	 * annotates a member relation
	 * @param container
	 * @param memberAccess
	 * @return the type of the member
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateMemberAccess(SymbolContainer container, MemberAccess memberAccess) throws InvalidIdentifierException {
		Symbol symParent = annotateExpression(container, memberAccess.getParent());
		assert(symParent.hasType(SymbolType.CLASS_OR_INTERFACE));
		ClassOrInterface symClassOrIFace = (ClassOrInterface) symParent;
		
		Symbol symChild = annotateExpression(symClassOrIFace, memberAccess.getChild());
		assert(symChild.hasType(SymbolType.CLASS_OR_INTERFACE)); //includes primitive types
		return symChild;
	}
	
	protected Symbol annotateUnaryOperation(SymbolContainer container, UnaryOperation operation) {
		switch(operation.getUnaryOperator()) {
			case MINUS:
			case PLUS:  
			case NOT:
			case POSTDEC:
			case POSTINC:
			case PREDEC:
			case PREINC:
		}
		//TODO
		return null;
	}
}
