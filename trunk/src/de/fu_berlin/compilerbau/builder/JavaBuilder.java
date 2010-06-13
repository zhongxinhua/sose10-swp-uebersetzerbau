package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 * @author Sam
 */

import java.io.IOException;
import java.util.List;

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.BreakStatement;
import de.fu_berlin.compilerbau.parser.CallStatement;
import de.fu_berlin.compilerbau.parser.Case;
import de.fu_berlin.compilerbau.parser.ChooseStatement;
import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.ContinueStatement;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.DoStatement;
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
import de.fu_berlin.compilerbau.parser.expressions.Type;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation;
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation.BinaryOperator;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation.UnaryOperator;
import de.fu_berlin.compilerbau.util.ErrorHandler;

public class JavaBuilder extends Builder {
	public JavaBuilder(AbstractSyntaxTree astree, String classpath) {
		super(astree, classpath);
	}

	@Override
	protected void buildClass(Class theclass) throws IOException {
		Module root = _astree.getRoot();
		_code.append("package " + root.getName() + ";\n");

		// check for import statements
		if (!theclass.getImports().isEmpty()) {
			for (ImportStatement importstmt : theclass.getImports()) {
				buildImportStatement(importstmt);
			}
		}

		_code.append("class " + theclass.getName() + " ");
		// check for optional attribute "super"
		if (theclass.getSuper() != null) {
			_code.append("extends " + theclass.getSuper());
		}
		// check for implement statements
		if (!theclass.getImplementations().isEmpty()) {
			for (ImplementStatement implementstmt : theclass
					.getImplementations()) {
				buildImplementStatement(implementstmt);
			}
			// Reset Boolean
			isFirstImplementStatement = true;
		}

		// JAVA-Class-Body Begin>
		_code.append(" {\n");

		for (DeclarationStatement decl : theclass.getDeclarations()) {
			buildDeclarationStatement(decl);
		}

		for (Function func : theclass.getFunctions()) {
			buildFunction(func);
		}

		_code.append("}\n");
		// JAVA-Class-Body End>
	}

	@Override
	protected void buildFunction(Function func) throws IOException {
		ErrorHandler.debugMsg(null, "DEBUG::buildFunction::" + 
			func.getClass().toString());
					
		_code.append("public ");
		if (func.isFinal()) {
			_code.append("final ");
		}
		if (func.isStatic()) {
			_code.append("static ");
		}

		_code.append(functionTypeToJavaString(func.getReturnType()) + " ");

		_code.append(func.getName() + " (");

		// Argumente der Funktion
		List<DeclarationStatement> args = func.getArguments();
		for (int i = 0; i < args.size(); ++i) {
			DeclarationStatement declStmt = args.get(i);
			_code.append(typeToJavaString(declStmt.getType()) + " ");
			_code.append(declStmt.getName());
			if (i + 1 < args.size())
				_code.append(", ");
		}

		_code.append(") {\n");
		for (Statement stmt : func.getBody()) {
			buildStatement(stmt);
		}

		_code.append("}\n");
	}


	protected void buildBreakStatement(BreakStatement obj) throws IOException {
		_code.append("break;\n");
	}


	protected void buildCallStatement(CallStatement obj) throws IOException {

	}

	protected void buildCase(Case obj) throws IOException {
		buildExpressionStatement(obj.getTest());
		_code.append(") {\n");
		for (Statement stmt : obj.getBody()) {
			buildStatement(stmt);
		}
		_code.append("} ");

	}
	
	protected void buildChooseStatement(ChooseStatement obj) throws IOException {
		boolean isFirstCase = true;
		for (Case concase : obj.getCases()) {
			if (isFirstCase) {
				isFirstCase = false;
				_code.append("if(");
				buildCase(concase);
			} else {
				_code.append("else if(");
				buildCase(concase);
			}
		}

	}

	@Override
	protected void buildContinueStatement(ContinueStatement obj)
			throws IOException {
		_code.append("continue;\n");

	}

	// ASSERT: this Declaration Statement DO NOT appear in <arguments> body
	protected void buildDeclarationStatement(DeclarationStatement decl)
			throws IOException {

		if (decl.isFinal()) {
			_code.append("static ");
		}
		if (decl.isFinal()) {
			_code.append("final ");
		}

		_code.append(typeToJavaString(decl.getType()) + " ");

		for (int i = 0; i < decl.getDimension(); ++i) {
			_code.append("[] ");
		}

		_code.append(decl.getName());

		if (decl.getValue() != null) {
			_code.append(" = ");
			buildExpressionStatement(decl.getValue());
		}
		/*
		 * optional TODO: Arrays else if(decl.isArray()) {
		 * _code.append(" = new " + Type.toJavaString(decl.getType())); for(int
		 * i=0; i<decl.getDimension(); ++i) { _code.append("[]"); } }
		 */
		_code.append(";\n");
	}

	@Override
	protected void buildDoStatement(DoStatement obj) throws IOException {
		_code.append("while(");
		buildExpressionStatement(obj.getTest());
		// WHILE-Body Begin>
		_code.append(") {\n");

		for (Statement stmt : obj.getBody()) {
			buildStatement(stmt);
		}
		// WHILE-Body End>
		_code.append("}\n");

	}

	boolean isFirstImplementStatement = true;

	protected void buildImplementStatement(ImplementStatement obj)
			throws IOException {
		if (isFirstImplementStatement) {
			_code.append("implements ");
			_code.append(obj.getName());
			isFirstImplementStatement = false;
		} else {
			_code.append(", " + obj.getName());
		}
	}

	protected void buildImportStatement(ImportStatement obj) throws IOException {
		_code.append("import ");
		_code.append(obj.getName());
		_code.append(";\n");
	}

	@Override
	protected void buildInterface(Interface obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildModule(Module obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildReturnStatement(ReturnStatement obj) throws IOException {
		_code.append("return ");
		buildExpressionStatement(obj.getValue());
		_code.append(";\n");
	}

	@Override
	protected void buildScopeStatement(ScopeStatement obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildStatement(Statement obj) throws IOException {
		ErrorHandler.debugMsg(null, "DEBUG::buildStatement::"
			+ obj.getClass().toString());
		
		if (obj instanceof BreakStatement) {
			buildBreakStatement((BreakStatement) obj);
		} else if (obj instanceof CallStatement) {
			buildCallStatement((CallStatement) obj);
		} else if (obj instanceof ChooseStatement) {
			buildChooseStatement((ChooseStatement) obj);
		} else if (obj instanceof ContinueStatement) {
			buildContinueStatement((ContinueStatement) obj);
		} else if (obj instanceof DeclarationStatement) {
			buildDeclarationStatement((DeclarationStatement) obj);
		} else if (obj instanceof DoStatement) {
			buildDoStatement((DoStatement) obj);
		} else if (obj instanceof ReturnStatement) {
			buildReturnStatement((ReturnStatement) obj);
		} else if (obj instanceof ScopeStatement) {
			buildScopeStatement((ScopeStatement) obj);
		} else if (obj instanceof SetStatement) {
			buildSetStatement((SetStatement) obj);
		} else {
			// ERROR
			System.err.println(this.getClass().toString()
					+ " - Should not happen - buildStatement::"
					+ obj.getClass().toString());
		}

	}

	protected void buildSetStatement(SetStatement obj) throws IOException {
		buildExpressionStatement(obj.getLValue());
		_code.append("=");
		buildExpressionStatement(obj.getRLValue());
		_code.append(";\n");
	}

	protected void buildExpressionStatement(Expression obj) throws IOException {
		ErrorHandler.debugMsg(null, "DEBUG::buildExpressionStatement::"
			+ obj.getClass().toString());
		
		if (obj instanceof ArrayAccess) {
			buildArrayAccessExpression((ArrayAccess) obj);
		} else if (obj instanceof BinaryOperation) {
			buildBinaryOperationExpression((BinaryOperation) obj);
		} else if (obj instanceof FloatLiteral) {
			buildFloatLiteralExpression((FloatLiteral) obj);
		} else if (obj instanceof FunctionCall) {
			buildFunctionCallExpression((FunctionCall) obj);
		} else if (obj instanceof Identifier) {
			buildIdentifierExpression((Identifier) obj);
		} else if (obj instanceof IntegerLiteral) {
			buildIntegerLiteralExpression((IntegerLiteral) obj);
		} else if (obj instanceof MemberAccess) {
			buildMemberAccessExpression((MemberAccess) obj);
		} else if (obj instanceof NullLiteral) {
			buildNullLiteralExpression((NullLiteral) obj);
		} else if (obj instanceof ObjectCreation) {
			buildObjectCreationExpression((ObjectCreation) obj);
		} else if (obj instanceof StringLiteral) {
			buildStringLiteralExpression((StringLiteral) obj);
		} else if (obj instanceof UnaryOperation) {
			buildUnaryOperationExpression((UnaryOperation) obj);
		} else {
			// ERROR
			System.err.println(this.getClass().toString()
					+ " - Should not happen - buildExpresseionStatement");
		}

	}

	protected void buildArrayAccessExpression(ArrayAccess obj)
			throws IOException {
		// Arrayname
		_code.append(obj.getName());
		_code.append('[');
		for (Expression indice : obj.getIndices()) {
			buildExpressionStatement(indice);
		}
		_code.append(']');
	}

	protected void buildBinaryOperationExpression(BinaryOperation obj)
			throws IOException {
		// Build Left
		buildExpressionStatement(obj.getLeftExpression());

		// Build Operator
		BinaryOperator tmp = obj.getBinaryOperator();
		if (tmp == BinaryOperator.ADD) {
			_code.append('+');
		} else if (tmp == BinaryOperator.AND) {
			_code.append("&&");
		} else if (tmp == BinaryOperator.BITWISE_AND) {
			_code.append('&');
		} else if (tmp == BinaryOperator.BITWISE_OR) {
			_code.append('|');
		} else if (tmp == BinaryOperator.BITWISE_XOR) {
			_code.append('^');
		} else if (tmp == BinaryOperator.DIVIDES) {
			_code.append('/');
		} else if (tmp == BinaryOperator.EQUAL) {
			_code.append("==");
		} else if (tmp == BinaryOperator.GREATER_EQUAL) {
			_code.append(">=");
		} else if (tmp == BinaryOperator.GREATER_THAN) {
			_code.append('>');
		} else if (tmp == BinaryOperator.LESS_EQUAL) {
			_code.append("<=");
		} else if (tmp == BinaryOperator.LESS_THAN) {
			_code.append('<');
		} else if (tmp == BinaryOperator.MODULOS) {
			_code.append('%');
		} else if (tmp == BinaryOperator.NOTEQUAL) {
			_code.append("!=");
		} else if (tmp == BinaryOperator.OR) {
			_code.append("||");
		} else if (tmp == BinaryOperator.SUBSTRACT) {
			_code.append('-');
		} else if (tmp == BinaryOperator.TIMES) {
			_code.append('*');
		} else {
			// ERROR
			System.err.println(this.getClass().toString()
					+ " - Should not happen - buildBinaryOperationExpression");
		}
		// Build Right
		buildExpressionStatement(obj.getRightExpression());

	}

	protected void buildFloatLiteralExpression(FloatLiteral obj)
			throws IOException {
		_code.append(String.valueOf(obj.getValue()+"f"));
	}

	protected void buildFunctionCallExpression(FunctionCall obj)
			throws IOException {
		_code.append(obj.getName() + "(");

		for (Expression argument : obj.getArguments()) {
			buildExpressionStatement(argument);
		}

		_code.append(")");
	}

	protected void buildIdentifierExpression(Identifier obj) throws IOException {
		_code.append(obj.getName());
	}

	protected void buildIntegerLiteralExpression(IntegerLiteral obj)
			throws IOException {
		_code.append(String.valueOf(obj.getValue()));
	}

	protected void buildMemberAccessExpression(MemberAccess obj)
			throws IOException {
		buildExpressionStatement(obj.getParent());
		_code.append('.');
		buildExpressionStatement(obj.getChild());

	}

	protected void buildNullLiteralExpression(NullLiteral obj)
			throws IOException {
		_code.append("null");

	}

	protected void buildObjectCreationExpression(ObjectCreation obj)
			throws IOException {
		// as ObjectCreation is a subclass of FunctionCall, use FunctionCall
		// Build
		buildFunctionCallExpression((FunctionCall) obj);
	}

	protected void buildStringLiteralExpression(StringLiteral obj)
			throws IOException {
		_code.append(obj.getValue());

	}

	protected void buildUnaryOperationExpression(UnaryOperation obj)
			throws IOException {
		// Build Operator
		UnaryOperator tmp = obj.getUnaryOperator();
		if (tmp == UnaryOperator.MINUS) {
			_code.append('-');
			buildExpressionStatement(obj.getExpression());
		} else if (tmp == UnaryOperator.NOT) {
			_code.append('!');
			buildExpressionStatement(obj.getExpression());
		} else if (tmp == UnaryOperator.PLUS) {
			_code.append('+');
			buildExpressionStatement(obj.getExpression());
		} else if (tmp == UnaryOperator.PREDEC) {
			_code.append("--");
			buildExpressionStatement(obj.getExpression());
		} else if (tmp == UnaryOperator.PREINC) {
			_code.append("++");
			buildExpressionStatement(obj.getExpression());
		} else if (tmp == UnaryOperator.POSTDEC) {
			buildExpressionStatement(obj.getExpression());
			_code.append("--");
		} else if (tmp == UnaryOperator.POSTINC) {
			buildExpressionStatement(obj.getExpression());
			_code.append("++");
		} else {
			// ERROR
			System.err.println(this.getClass().toString()
					+ " - Should not happen - buildBinaryOperationExpression");
		}

	}

	private String typeToJavaString(Type type) {
		if (type == Type.STRING) {
			return "String";
		} else if (type == Type.INTEGER) {
			return "int";
		} else if (type == Type.FLOAT) {
			return "float";
		} else {
			return "null";
		}
	}

	private String functionTypeToJavaString(Type type) {
		if (type == Type.STRING) {
			return "String";
		} else if (type == Type.INTEGER) {
			return "int";
		} else if (type == Type.FLOAT) {
			return "float";
		} else {
			return "void";
		}
	}

}
