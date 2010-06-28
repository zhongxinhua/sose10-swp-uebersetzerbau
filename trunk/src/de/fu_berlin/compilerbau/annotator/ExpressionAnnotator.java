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
import de.fu_berlin.compilerbau.symbolTable.Constructor;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.QualifiedSymbol;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
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
	 * @param scope
	 * @param expression
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateExpression(Scope scope, Expression expression) throws InvalidIdentifierException {
		if(expression instanceof ArrayAccess)
			return annotateArrayAccess(scope, (ArrayAccess) expression);
		else if(expression instanceof ArrayCreation)
			return annotateArrayCreation(scope, (ArrayCreation) expression);
		else if(expression instanceof BinaryOperation)
			return annotateBinaryOperation(scope, (BinaryOperation) expression);
		else if(expression instanceof Literal) 
			return annotateLiteral(scope, (Literal) expression);
		else if(expression instanceof FunctionCall)
			return annotateFunctionCall(scope, (FunctionCall) expression);
		else if(expression instanceof Identifier)
			return annotateIdentifier(scope, (Identifier) expression);
		else if(expression instanceof MemberAccess)
			return annotateMemberAccess(scope, (MemberAccess) expression);
		else if(expression instanceof UnaryOperation)
			return annotateUnaryOperation(scope, (UnaryOperation) expression);
		throw new RuntimeException("will never be reached... cookies");
	}
	
	protected Symbol annotateArrayAccess(Scope scope, ArrayAccess access) throws InvalidIdentifierException {
		//add mention
		Symbol symbol = scope.getQualifiedSymbol(access.getName());
		scope.addMention(symbol, access);
		
		//check indices
		for(Expression index : access.getIndices()) {
			Symbol symIndex = annotateExpression(scope, index);
		}
		
		return symbol;
	}
	
	protected Symbol annotateArrayCreation(Scope scope, ArrayCreation creation) throws InvalidIdentifierException {
		Symbol firstType = null;
		for(Expression expression : creation.getElements()) {
			Symbol type = annotateExpression(scope, expression);
		}
		return null;
	}
	
	
	protected Symbol annotateBinaryOperation(Scope scope, BinaryOperation operation) {
		return null;
		//TODO
	}
	
	/**
	 * annotates a literal with its type
	 * @param scope
	 * @param literal
	 * @return the type of the literal
	 */
	protected Symbol annotateLiteral(Scope scope, Literal literal) {
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
	 * @param scope
	 * @param call
	 * @return the return type of the function
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateFunctionCall(Scope scope, FunctionCall call) throws InvalidIdentifierException {
		//check symbol
		final PositionString name = call.getName();
		Symbol symbol = scope.getQualifiedSymbol(name);
		ErrorHandler.debugMsg(call, "annotating function call \""+name+"\"");		
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
			Symbol actualType = annotateExpression(scope, exprArg);
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
	 * @param scope
	 * @param identifier
	 * @return the type of the identifier
	 * @throws InvalidIdentifierException
	 */
	protected Symbol annotateIdentifier(Scope scope, Identifier identifier) throws InvalidIdentifierException {
		QualifiedSymbol symbol = scope.getQualifiedSymbol(identifier.getName());
		assert(symbol.hasType(SymbolType.VARIABLE));
		identifier.setSymbol(symbol);
		scope.addMention(symbol, identifier);
		return ((Variable) symbol).getVariableType();
	}
	
	protected Symbol annotateMemberAccess(Scope scope, MemberAccess memberAccess) throws InvalidIdentifierException {
		Symbol symParent = annotateExpression(scope, memberAccess.getParent());
		assert(symParent.hasType(SymbolType.CLASS_OR_INTERFACE));
			
		return null;
	}
	
	protected Symbol annotateUnaryOperation(Scope scope, UnaryOperation operation) {
		switch(operation.getUnaryOperator()) {
			case MINUS:
			case PLUS:  
			case NOT:
			case POSTDEC:
			case POSTINC:
			case PREDEC:
			case PREINC:
		}
		return null;
	}
}
