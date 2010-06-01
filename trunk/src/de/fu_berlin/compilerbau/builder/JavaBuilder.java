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
import de.fu_berlin.compilerbau.parser.expressions.Literal;
import de.fu_berlin.compilerbau.parser.expressions.MemberAccess;
import de.fu_berlin.compilerbau.parser.expressions.NullLiteral;
import de.fu_berlin.compilerbau.parser.expressions.ObjectCreation;
import de.fu_berlin.compilerbau.parser.expressions.StringLiteral;
import de.fu_berlin.compilerbau.parser.expressions.Type;
import de.fu_berlin.compilerbau.parser.expressions.UnaryOperation;

public class JavaBuilder extends Builder {

	public JavaBuilder(AbstractSyntaxTree astree, String classpath) {
		super(astree, classpath);
	}

	@Override
	protected void buildClass(Class theclass) throws IOException {
		Module root = _astree.getRoot();
		_code.append("package " + root.getName() + ";\n");

		_code.append("class " + theclass.getName() + " ");
		
		// TODO: super auswerten, wenn irgendwann im SyntaxBaum vorhanden
		/*if (theclass.getSuper() != null) {
			_code.append("extends " + theclass.getSuper());
		}*/
		
		_code.append(" {\n");
		
		for(DeclarationStatement decl : theclass.getDeclerations()) {
			buildDecleration(decl);
		}

		for (Function func : theclass.getFunctions()) {
			buildFunction(func);
		}

		_code.append("}\n");
	}

	@Override
	protected void buildDecleration(DeclarationStatement decl)
			throws IOException {
		
		if(decl.isFinal()) {
			_code.append("static ");
		}
		if(decl.isFinal()) {
			_code.append("final ");
		}
		
		_code.append(Type.toJavaString(decl.getType()) + " ");
		
		for(int i=0; i<decl.getDimension(); ++i) {
			_code.append("[] ");
		}
		
		_code.append(decl.getName());
		
		if(decl.getValue() != null) {
			_code.append(" = " + decl.getValue());
		}
		/*
		 * optional TODO: Arrays
		else if(decl.isArray()) {
				_code.append(" = new " + Type.toJavaString(decl.getType()));
				for(int i=0; i<decl.getDimension(); ++i) {
					_code.append("[]");
				}
		}*/
		_code.append(";\n");
	}

	@Override
	protected void buildFunction(Function func) throws IOException {
		_code.append("public ");

		_code.append(Type.toJavaString(func.getReturnType()) + " ");

		_code.append(func.getName() + " (");
		
		// Argumente der Funktion
		List<DeclarationStatement> args = func.getArguments();
		for(int i=0; i<args.size(); ++i) {
			DeclarationStatement declStmt = args.get(i);
			_code.append(Type.toJavaString(declStmt.getType()) + " ");
			_code.append(declStmt.getName());
			if(i+1<args.size()) _code.append(", ");
		}
		
		_code.append(") {\n");

		for(Statement stmt : func.getBody()) {
			buildStatement(stmt);
		}
		
		_code.append("}\n");
	}

	@Override
	protected void buildBreakStatement(BreakStatement obj) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void buildCallStatement(CallStatement obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildCase(Case obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildChooseStatement(ChooseStatement obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildContinueStatement(ContinueStatement obj)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildDeclarationStatement(DeclarationStatement obj)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildDoStatement(DoStatement obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildImplementStatement(ImplementStatement obj)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildImportStatement(ImportStatement obj) throws IOException {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildScopeStatement(ScopeStatement obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildStatement(Statement obj) throws IOException {
		if (obj instanceof SetStatement) {
			buildSetStatement((SetStatement) obj);
		} else if (obj instanceof BreakStatement) {
			buildBreakStatement((BreakStatement) obj);
		} else if (obj instanceof CallStatement) {
			buildCallStatement((CallStatement) obj);
		} else if (obj instanceof ChooseStatement) {
			buildChooseStatement((ChooseStatement) obj);
		} else if (obj instanceof ContinueStatement) {
			buildContinueStatement((ContinueStatement) obj);
		} else if (obj instanceof DeclarationStatement) {
			buildDeclarationStatement((DeclarationStatement) obj);
		}else if (obj instanceof DoStatement) {
			buildDoStatement((DoStatement) obj);
		}else if (obj instanceof ScopeStatement) {
			buildScopeStatement((ScopeStatement) obj);
		}else{
			//ERROR
			System.err.println(this.getClass().toString()+" - Should not happen - buildStatement");
		}

	}

	protected void buildSetStatement(SetStatement obj) throws IOException {
		buildExpressionStatement(obj.lvalue);
		_code.append("=");
		buildExpressionStatement(obj.rvalue);
	}

	@Override
	protected void buildExpressionStatement(Expression obj) throws IOException {
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
		}else if (obj instanceof Literal) {
			buildLiteralExpression((Literal) obj);
		}else if (obj instanceof MemberAccess) {
			buildMemberAccessExpression((MemberAccess) obj);
		}else if (obj instanceof NullLiteral) {
			buildNullLiteralExpression((NullLiteral) obj);
		}else if (obj instanceof ObjectCreation) {
			buildObjectCreationExpression((ObjectCreation) obj);
		}else if (obj instanceof StringLiteral) {
			buildStringLiteralExpression((StringLiteral) obj);
		}else if (obj instanceof UnaryOperation) {
			buildUnaryOperationExpression((UnaryOperation) obj);
		}else{
			//ERROR
			System.err.println(this.getClass().toString()+" - Should not happen - buildExpresseionStatement");
		}

	}

	@Override
	protected void buildArrayAccessExpression(ArrayAccess obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildBinaryOperationExpression(BinaryOperation obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildFloatLiteralExpression(FloatLiteral obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildFunctionCallExpression(FunctionCall obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildIdentifierExpression(Identifier obj) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildIntegerLiteralExpression(IntegerLiteral obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildLiteralExpression(Literal obj) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildMemberAccessExpression(MemberAccess obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildNullLiteralExpression(NullLiteral obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildObjectCreationExpression(ObjectCreation obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildStringLiteralExpression(StringLiteral obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildUnaryOperationExpression(UnaryOperation obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
