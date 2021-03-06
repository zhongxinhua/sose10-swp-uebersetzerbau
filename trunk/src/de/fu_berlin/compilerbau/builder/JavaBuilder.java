package de.fu_berlin.compilerbau.builder;

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
import de.fu_berlin.compilerbau.parser.ForEachStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementsStatement;
import de.fu_berlin.compilerbau.parser.ImportStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.ReturnStatement;
import de.fu_berlin.compilerbau.parser.ScopeStatement;
import de.fu_berlin.compilerbau.parser.SetStatement;
import de.fu_berlin.compilerbau.parser.Statement;
import de.fu_berlin.compilerbau.parser.expressions.ArrayAccess;
import de.fu_berlin.compilerbau.parser.expressions.ArrayCreation;
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
import de.fu_berlin.compilerbau.parser.expressions.BinaryOperation.BinaryOperator;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation.UnaryOperator;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * @author stefan
 * @author Sam
 */
public class JavaBuilder extends Builder {
	private int _scannerId = 0; 
	
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
			for (ImplementsStatement implementstmt : theclass
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

		if(func.getReturnType() != null) {
			_code.append(func.getReturnType() + " ");
		} else {
			_code.append("void ");
		}

		_code.append(func.getName() + " (");

		// Argumente der Funktion
		List<DeclarationStatement> args = func.getArguments();
		for (int i = 0; i < args.size(); ++i) {
			DeclarationStatement declStmt = args.get(i);
			_code.append(declStmt.getType() + " ");
			if(declStmt.isArray()) {
				_code.append("[] ");
			}
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
		Expression call = obj.getCall();
		if(call instanceof FunctionCall) {
			FunctionCall fc = (FunctionCall)call;
			if(fc.getName().equals("print")) {
				_code.append("System.out.println(");
				for(Expression e : fc.getArguments()) {
					buildExpressionStatement(e);
				}
				_code.append(");\n");
				return;
			} else if (fc.getName().equals("read")) {
				String scanner = "scanner"+ _scannerId++;
				_code.append("java.util.Scanner " + scanner + " = new java.util.Scanner(System.in);\n");
				_code.append(obj.getValue() + " = " + scanner + ".nextInt();\n");
				return;
			}
		}
		buildExpressionStatement(call);
		_code.append(";\n");
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

	// ASSERT: this Declaration Statement DO NOT appear in <arguments> body, since these are parameters
	protected void buildDeclarationStatement(DeclarationStatement decl)
			throws IOException {

		if (decl.isFinal()) {
			_code.append("static ");
		}
		if (decl.isFinal()) {
			_code.append("final ");
		}

		_code.append(decl.getType());
		
		for (int i = 0; i < decl.getDimension(); ++i) {
			_code.append("[] ");
		}
		_code.append(" ");

		_code.append(decl.getName());

		if (decl.getValue() != null) {
			
			_code.append(" = ");
			boolean isString = decl.getType().equals("String");
			if(isString){
				_code.append("new String(\"");
			}
			buildExpressionStatement(decl.getValue());
			if(isString){
				_code.append("\")");
			}
		}
		
		else if(decl.isArray()) {

				_code.append("= null"); 
			
		}
		
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
	
	@Override
	protected void buildForEachStatement(ForEachStatement obj) throws IOException {
		//TODO needs type from Symboltable
		/*_code.append("for("")");
		_code.append("");*/
	}

	boolean isFirstImplementStatement = true;

	protected void buildImplementStatement(ImplementsStatement obj)
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
		_code.append("public interface ");
		_code.append(obj.getName());
		_code.append(" {\n");
		for(Function func : obj.getFunctions()) {
			buildFunction(func);
		}
		_code.append("\n}");
	}

	@Override
	protected void buildModule(Module obj) throws IOException {
		// There is no need to implement this Function for Java,
		// package structure is handled in buildClass
	}

	@Override
	protected void buildReturnStatement(ReturnStatement obj) throws IOException {
		_code.append("return ");
		buildExpressionStatement(obj.getValue());
		_code.append(";\n");
	}

	@Override
	protected void buildScopeStatement(ScopeStatement obj) throws IOException {
		// this is currently not supported
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
		} else if (obj instanceof ForEachStatement) {
			buildForEachStatement((ForEachStatement) obj); 
		} else if (obj instanceof ReturnStatement) {
			buildReturnStatement((ReturnStatement) obj);
		} else if (obj instanceof ScopeStatement) {
			buildScopeStatement((ScopeStatement) obj);
		} else if (obj instanceof SetStatement) {
			buildSetStatement((SetStatement) obj);
		} else {
			// ERROR
			throw new RuntimeException("(Internal) Invalid statement: " + obj);
		}

	}

	protected void buildSetStatement(SetStatement obj) throws IOException {
		buildExpressionStatement(obj.getLValue());
		_code.append("=");
		buildExpressionStatement(obj.getRValue());
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
		} else if (obj instanceof ArrayCreation) {
			buildArrayCreation((ArrayCreation)obj);
		} else {
			// ERROR
			throw new RuntimeException("(Internal) Invalid expression: " + obj);
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

	protected void buildArrayCreation(ArrayCreation obj)
			throws IOException {
		List<Expression> elements = obj.getElements();
		_code.append("{");
		for(int i=0;i<elements.size(); ++i) {
			buildExpressionStatement(elements.get(i));
			if(!(i == elements.size()-1)) {
				_code.append(",");
			}
		}
		_code.append("}");
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

		List<Expression> args = obj.getArguments();
		for(int i=0; i<args.size(); ++i) {
			buildExpressionStatement(args.get(i));
			if(!(i == args.size()-1)) {
				_code.append(",");
			}
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
	
		_code.append(obj.getValue().toString().replace('\'','\"'));

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
}
