package de.fu_berlin.compilerbau.builder;

/**
 * Abstract Class for the builder
 * 
 * For the design pattern refer Wikipedia: 
 * http://en.wikipedia.org/wiki/Builder_pattern
 * 
 * @author stefan
 * @author Sam
 */

import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.BreakStatement;
import de.fu_berlin.compilerbau.parser.CallStatement;
import de.fu_berlin.compilerbau.parser.Case;
import de.fu_berlin.compilerbau.parser.ChooseStatement;
import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.ContinueStatement;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.DoStatement;
import de.fu_berlin.compilerbau.parser.ForEachStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementStatement;
import de.fu_berlin.compilerbau.parser.ImportStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.ReturnStatement;
import de.fu_berlin.compilerbau.parser.ScopeStatement;
import de.fu_berlin.compilerbau.parser.SetStatement;
import de.fu_berlin.compilerbau.parser.Statement;
import de.fu_berlin.compilerbau.parser.expressions.ArrayAccess;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.parser.expressions.FloatLiteral;
import de.fu_berlin.compilerbau.parser.expressions.FunctionCall;
import de.fu_berlin.compilerbau.parser.expressions.Identifier;
import de.fu_berlin.compilerbau.parser.expressions.IntegerLiteral;
import de.fu_berlin.compilerbau.parser.expressions.MemberAccess;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.ObjectCreation;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation;

public abstract class Builder {
	
	protected final AbstractSyntaxTree _astree;
	protected final String[] _classpath;
	
	protected PrintStream _code = null;
	
	/** 
	 * @param astree The Abstract Syntaxtree with the Code
	 * @param classpath
	 */
	public Builder(AbstractSyntaxTree astree, String classpath) {
		_astree = astree;
		
		if(classpath == null || classpath.isEmpty()) {
			_classpath = new String[0];
		} else {
			_classpath = Pattern.compile(":").split(classpath);
		}
	}

	/**
	 * Build a whole Class with all parts
	 * 
	 * @param theclass The Class to build
	 * @throws IOException
	 */
	protected abstract void buildClass(Class theclass) throws IOException;
	
	/**
	 * Build a whole Function with all parts
	 * 
	 * @param function the Function to build
	 * @throws IOException
	 */
	protected abstract void buildFunction(Function function) throws IOException;

	/*
	 * START>List of Statements to build
	 */
	protected abstract void buildBreakStatement(BreakStatement obj) throws IOException;
	protected abstract void buildCallStatement(CallStatement obj) throws IOException;
	protected abstract void buildCase(Case obj) throws IOException;
	protected abstract void buildChooseStatement(ChooseStatement obj) throws IOException;
	protected abstract void buildContinueStatement(ContinueStatement obj) throws IOException;
	protected abstract void buildDeclarationStatement(DeclarationStatement obj) throws IOException;
	protected abstract void buildDoStatement(DoStatement obj) throws IOException;
	protected abstract void buildForEachStatement(ForEachStatement obj) throws IOException;
	protected abstract void buildExpressionStatement(Expression obj) throws IOException;
	protected abstract void buildImplementStatement(ImplementStatement obj) throws IOException;
	protected abstract void buildImportStatement(ImportStatement obj) throws IOException;
	protected abstract void buildInterface(Interface obj) throws IOException;
	protected abstract void buildModule(Module obj) throws IOException;
	protected abstract void buildReturnStatement(ReturnStatement obj) throws IOException;
	protected abstract void buildScopeStatement(ScopeStatement obj) throws IOException;
	protected abstract void buildStatement(Statement obj) throws IOException;
	protected abstract void buildSetStatement(SetStatement obj) throws IOException;
	/*
	 * END>List of Statements to build
	 */
	
	
	/*
	 * START>List of Expressions to build
	*/
	protected abstract void buildArrayAccessExpression(ArrayAccess obj) throws IOException;
	protected abstract void buildBinaryOperationExpression(BinaryOperation obj)throws IOException;
	protected abstract void buildFloatLiteralExpression(FloatLiteral obj)throws IOException;
	protected abstract void buildFunctionCallExpression(FunctionCall obj)throws IOException;
	protected abstract void buildIdentifierExpression(Identifier obj) throws IOException;
	protected abstract void buildIntegerLiteralExpression(IntegerLiteral obj) throws IOException;
	protected abstract void buildMemberAccessExpression(MemberAccess obj) throws IOException;
	protected abstract void buildNullLiteralExpression(NullLiteral obj) throws IOException;
	protected abstract void buildObjectCreationExpression(ObjectCreation obj) throws IOException;
	protected abstract void buildStringLiteralExpression(StringLiteral obj) throws IOException;
	protected abstract void buildUnaryOperationExpression(UnaryOperation obj) throws IOException;
	/*
	 * END>List of Expressions to build
	 */

	public void setCode(PrintStream code) {
		_code = code;
	}
	
	/**
	 * Getter for the Code
	 * 
	 * @return the complete Code in the specific language
	 */
	public Appendable getCode() {
		return _code;
	}
	
}
