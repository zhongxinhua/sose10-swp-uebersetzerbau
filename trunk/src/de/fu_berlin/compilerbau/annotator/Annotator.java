package de.fu_berlin.compilerbau.annotator;

import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.BreakStatement;
import de.fu_berlin.compilerbau.parser.CallStatement;
import de.fu_berlin.compilerbau.parser.Case;
import de.fu_berlin.compilerbau.parser.ChooseStatement;
import de.fu_berlin.compilerbau.parser.ContinueStatement;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.DoStatement;
import de.fu_berlin.compilerbau.parser.ExtendsStatement;
import de.fu_berlin.compilerbau.parser.ForEachStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementsStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.ReturnStatement;
import de.fu_berlin.compilerbau.parser.ScopeStatement;
import de.fu_berlin.compilerbau.parser.SetStatement;
import de.fu_berlin.compilerbau.parser.Statement;
import de.fu_berlin.compilerbau.symbolTable.ArrayType;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Member;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ContainerSymbolsException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.Visibility;

public class Annotator {
	protected Runtime runtime;
	protected ExpressionAnnotator exprAnnotator;
	protected final static Modifier PUBLIC_MODIFIER = GetModifier.getModifier(
			Visibility.PUBLIC, false, false, false);

	private static class ExpressionMention {
		Scope scope;
		Statement exprStatement;
	}

	private List<ExpressionMention> mentions;

	private void rememberExpression(Scope scope, Statement stmt) {
		ExpressionMention mention = new ExpressionMention();
		mention.scope = scope;
		mention.exprStatement = stmt;
		mentions.add(mention);
	}

	public Annotator(Runtime runtime, AbstractSyntaxTree ast)
			throws InvalidIdentifierException, DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		this.runtime = runtime;
		this.mentions = new LinkedList<ExpressionMention>();
		this.exprAnnotator = new ExpressionAnnotator(runtime);
		firstPass(ast);
		secondPass();
	}

	private void firstPass(AbstractSyntaxTree ast) {
		try {
			annotateModule(ast.getRoot());
			runtime.qualifyAllSymbols();
		} catch(Exception e) {
			throw new RuntimeException(e);
		} 
		
		/*catch (ContainerSymbolsException e) {
			ErrorHandler.error(null, "was?");
			e.printStackTrace();
		} catch (WrongModifierException e) {
			ErrorHandler.error(null, "was? 2");
			e.printStackTrace();
		} catch (InvalidIdentifierException e) {
			ErrorHandler.error(null, "invalid identifier");
			e.printStackTrace();
		}*/
	}

	private void secondPass() throws InvalidIdentifierException,
			DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		for (ExpressionMention mention : mentions)
			reannotateStatement(mention.scope, mention.exprStatement);
	}

	/**
	 * annotates a module node in the syntax tree
	 * 
	 * @param module
	 * @throws ContainerSymbolsException
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 */
	protected void annotateModule(Module module)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// Module
		Package symModule = runtime.addPackage(module.getName(),
				PUBLIC_MODIFIER);
		module.setSymbol(symModule);

		// Interfaces
		for (Interface iface : module.getInterfaces())
			annotateInterface(symModule, iface);

		// Classes
		for (de.fu_berlin.compilerbau.parser.Class clazz : module.getClasses())
			annotateClass(symModule, clazz);
	}

	/**
	 * annotates an interface node in the syntax tree
	 * 
	 * @param pack
	 * @param iface
	 * @throws ContainerSymbolsException
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 */
	protected void annotateInterface(Package pack, Interface iface)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// füge Erweiterungen ein
		List<Symbol> extends_ = new LinkedList<Symbol>();
		for (ExtendsStatement ex : iface.getExtensions()) {
			Symbol sym = runtime.tryGetQualifiedSymbol(ex.getName());
			ex.setSymbol(sym);
			extends_.add(sym);
		}

		// füge Interface ein
		de.fu_berlin.compilerbau.symbolTable.Interface symInterface = null;
		symInterface = pack.addInterface(iface.getName(), extends_.iterator(),
				PUBLIC_MODIFIER);
		iface.setSymbol(symInterface);

		// füge Funktionen ein
		for (Function func : iface.getFunctions())
			annotateFunction(symInterface, func);
	}

	/**
	 * annotates a class node in the syntax tree
	 * 
	 * @param pack
	 * @param class_
	 * @throws ContainerSymbolsException
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 */
	protected void annotateClass(Package pack,
			de.fu_berlin.compilerbau.parser.Class class_)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// füge Elternklasse ein
		Symbol symParent = null;
		if (class_.getSuper() != null)
			symParent = runtime.tryGetQualifiedSymbol(class_.getSuper());

		// füge Interfaces ein
		List<Symbol> implements_ = new LinkedList<Symbol>();
		for (ImplementsStatement impl : class_.getImplementations()) {
			Symbol sym = null;
			sym = runtime.tryGetQualifiedSymbol(impl.getName());
			impl.setSymbol(sym);
			implements_.add(sym);
		}

		// füge Klasse ein
		de.fu_berlin.compilerbau.symbolTable.Class symClass = null;
		symClass = pack.addClass(class_.getName(), symParent, implements_
				.iterator(), PUBLIC_MODIFIER);
		class_.setSymbol(symClass);

		// füge Attribute ein
		for (DeclarationStatement decl : class_.getDeclarations())
			annotateMember(symClass, decl);

		// füge Funktionen ein
		for (Function func : class_.getFunctions())
			annotateFunction(symClass, func);
	}

	/**
	 * annotates a declaration node as member of a class in the syntax tree
	 * 
	 * @param class_
	 * @param decl
	 * @throws InvalidIdentifierException
	 * @throws ContainerSymbolsException
	 * @throws WrongModifierException
	 */
	protected void annotateMember(
			de.fu_berlin.compilerbau.symbolTable.Class class_,
			DeclarationStatement decl) throws InvalidIdentifierException,
			ContainerSymbolsException, WrongModifierException {
		Symbol type = runtime.tryGetQualifiedSymbol(decl.getType());
		if (decl.isArray())
			type = runtime.getArrayType(type, decl.getDimension());
		Modifier modifier = GetModifier.getModifier(Visibility.PUBLIC, decl
				.isStatic(), decl.isFinal(), false);
		Member symMember = class_.addMember(decl.getName(), type, modifier);
		decl.setSymbol(symMember);
	}

	/**
	 * annotates a function node of a class or interface in the syntax tree
	 * 
	 * @param owner
	 * @param function
	 * @throws ContainerSymbolsException
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 */
	protected void annotateFunction(ClassOrInterface owner, Function function)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// ermittle Rückgabewert
		Symbol symReturnType;
		if(function.getReturnType()!=null)
			symReturnType = runtime.tryGetQualifiedSymbol(function.getReturnType());
		else
			symReturnType = runtime.getVoid();
		if (function.returnsArray())
			symReturnType = runtime.getArrayType(symReturnType, function
					.getReturnTypeDimension());

		// ermittle Parameter
		List<Variable> parameters = new LinkedList<Variable>();
		for (DeclarationStatement decl : function.getArguments()) {
			Symbol type = runtime.tryGetQualifiedSymbol(decl.getType());
			if (decl.isArray())
				type = runtime.getArrayType(type, decl.getDimension());
			Modifier modifier = GetModifier.getModifier(Visibility.PUBLIC, decl
					.isStatic(), decl.isFinal(), false);
			Variable var = runtime.getNewVariableForParameter(decl.getName(),
					type, modifier);
			parameters.add(var);
			decl.setSymbol(var);
		}

		// füge Funktion ein
		Method symMethod = owner.addMethod(function.getName(), symReturnType,
				parameters.iterator(), GetModifier.getModifier(
						Visibility.PUBLIC, function.isStatic(), function
								.isStatic(), false));
		function.setSymbol(symMethod);

		// füge Statements ein
		if (function.hasBody())
			annotateStatements(symMethod.getScope(), function.getBody());
	}

	/**
	 * annotates a list of statements
	 * 
	 * @param scope
	 * @param body
	 * @throws DuplicateIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws InvalidIdentifierException
	 * @throws WrongModifierException
	 */
	protected void annotateStatements(Scope scope, List<Statement> body)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			InvalidIdentifierException, WrongModifierException {
		for (Statement stmt : body) {
			if (stmt instanceof BreakStatement)
				annotateBreakStatement(scope, (BreakStatement) stmt);
			else if (stmt instanceof ContinueStatement)
				annotateContinueStatement(scope, (ContinueStatement) stmt);
			else if (stmt instanceof CallStatement)
				annotateCallStatement(scope, (CallStatement) stmt);
			else if (stmt instanceof ReturnStatement)
				annotateReturnStatement(scope, (ReturnStatement) stmt);
			else if (stmt instanceof DeclarationStatement)
				annotateDeclarationStatement(scope, (DeclarationStatement) stmt);
			else if (stmt instanceof ChooseStatement)
				annotateChooseStatement(scope, (ChooseStatement) stmt);
			else if (stmt instanceof DoStatement)
				annotateDoStatement(scope, (DoStatement) stmt);
			else if (stmt instanceof ForEachStatement)
				annotateForEachStatement(scope, (ForEachStatement) stmt);
			else if (stmt instanceof ScopeStatement)
				annotateScopeStatement(scope, (ScopeStatement) stmt);
			else if (stmt instanceof SetStatement)
				annotateSetStatement(scope, (SetStatement) stmt);
			else
				ErrorHandler.error(null, "unknown statement ("
						+ stmt.getClass().toString() + ")");
		}
	}

	/**
	 * re-annotates a list of statements (second pass) for fetching expressions
	 * 
	 * @param scope
	 * @param body
	 * @throws InvalidIdentifierException
	 * @throws DuplicateIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws InvalidIdentifierException
	 * @throws WrongModifierException
	 * @throws ShadowedIdentifierException
	 * @throws DuplicateIdentifierException
	 * @throws WrongModifierException
	 */
	protected void reannotateStatement(Scope scope, Statement stmt)
			throws InvalidIdentifierException, DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		if (stmt instanceof CallStatement)
			reannotateCallStatement(scope, (CallStatement) stmt);
		else if (stmt instanceof ReturnStatement)
			reannotateReturnStatement(scope, (ReturnStatement) stmt);
		else if (stmt instanceof DeclarationStatement)
			reannotateDeclarationStatement(scope, (DeclarationStatement) stmt);
		else if (stmt instanceof Case)
			reannotateCaseStatement(scope, (Case) stmt);
		else if (stmt instanceof DoStatement)
			reannotateDoStatement(scope, (DoStatement) stmt);
		else if (stmt instanceof ForEachStatement)
			reannotateForEachStatement(scope, (ForEachStatement) stmt);
		else if (stmt instanceof SetStatement)
			reannotateSetStatement(scope, (SetStatement) stmt);
	}

	/**
	 * annotates a break statement
	 * 
	 * @param scope
	 * @param stmt
	 */
	protected void annotateBreakStatement(Scope scope, BreakStatement stmt) { /*
																			 * do
																			 * nothing
																			 */
	}

	/**
	 * annotates a continue statement
	 * 
	 * @param scope
	 * @param stmt
	 */
	protected void annotateContinueStatement(Scope scope, ContinueStatement stmt) { /*
																					 * do
																					 * nothing
																					 */
	}

	/**
	 * annotates a CallStatement
	 * 
	 * @param scope
	 * @param stmt
	 */
	protected void annotateCallStatement(Scope scope, CallStatement stmt) {
		rememberExpression(scope, stmt);
	}

	/**
	 * annotates a CallStatement, the Statement will hold its type after this
	 * operation
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 */
	protected void reannotateCallStatement(Scope scope, CallStatement stmt)
			throws InvalidIdentifierException {
		stmt.setSymbol(exprAnnotator.annotateExpression(scope, stmt.getCall()));
	}

	/**
	 * annotates a ReturnStatement
	 * 
	 * @param scope
	 * @param stmt
	 */
	protected void annotateReturnStatement(Scope scope, ReturnStatement stmt) {
		rememberExpression(scope, stmt);
	}

	/**
	 * re-annotates a ReturnStatment (second pass)
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 */
	protected void reannotateReturnStatement(Scope scope, ReturnStatement stmt)
			throws InvalidIdentifierException {
		// TODO get functions return type
		if (stmt.getValue() != null)
			exprAnnotator.annotateExpression(scope, stmt.getValue());
		// TODO cecking types
	}

	/**
	 * annotates a declaration statement
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 * @throws DuplicateIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws WrongModifierException
	 */
	protected void annotateDeclarationStatement(Scope scope,
			DeclarationStatement stmt) throws InvalidIdentifierException,
			DuplicateIdentifierException, ShadowedIdentifierException,
			WrongModifierException {
		// add variable to scope
		Symbol symType = runtime.tryGetQualifiedSymbol(stmt.getType());
		scope.addVariable(stmt.getName(), symType, PUBLIC_MODIFIER);

		// note expression
		if(stmt.getValue() != null)
			rememberExpression(scope, stmt);
	}

	/**
	 * re-annotates the DeclarationStatement (second pass)
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 */
	protected void reannotateDeclarationStatement(Scope scope,
			DeclarationStatement stmt) throws InvalidIdentifierException {
		exprAnnotator.annotateExpression(scope, stmt.getValue());
		// TODO check types
	}

	/**
	 * annotates a choose statement
	 * 
	 * @param scope
	 *            the scope, where the choose statement lies in
	 * @param stmt
	 *            the choose statement
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws DuplicateIdentifierException
	 */
	protected void annotateChooseStatement(Scope scope, ChooseStatement stmt)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			InvalidIdentifierException, WrongModifierException {
		scope = scope.addScope();
		stmt.setSymbol(scope);
		for (Case case_ : stmt.getCases()) {
			// create scope
			Scope caseScope = scope.addScope();
			case_.setSymbol(caseScope);

			// note expression
			rememberExpression(caseScope, case_);

			// proceed with statements
			annotateStatements(caseScope, case_.getBody());
		}
	}

	/**
	 * re-annotates a Case (second pass)
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 */
	protected void reannotateCaseStatement(Scope scope, Case stmt)
			throws InvalidIdentifierException {
		exprAnnotator.annotateExpression(scope, stmt.getTest());
	}

	/**
	 * annotates a do statement
	 * 
	 * @param scope
	 *            the scope, where the do statement lies in
	 * @param stmt
	 *            the do statement
	 * @throws WrongModifierException
	 * @throws InvalidIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws DuplicateIdentifierException
	 */
	protected void annotateDoStatement(Scope scope, DoStatement stmt)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			InvalidIdentifierException, WrongModifierException {
		// create a scope for the loop
		scope = scope.addScope();
		stmt.setSymbol(scope);

		// note test expression
		rememberExpression(scope, stmt);

		// proceed with body statements
		annotateStatements(scope, stmt.getBody());
	}

	/**
	 * re-annotates a DoStatement (second pass)
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 */
	protected void reannotateDoStatement(Scope scope, DoStatement stmt)
			throws InvalidIdentifierException {
		exprAnnotator.annotateExpression(scope, stmt.getTest());
	}

	/**
	 * annotates a ForEachStatement
	 * 
	 * @param scope
	 * @param stmt
	 * @throws DuplicateIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws InvalidIdentifierException
	 * @throws WrongModifierException
	 */
	protected void annotateForEachStatement(Scope scope, ForEachStatement stmt)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			InvalidIdentifierException, WrongModifierException {
		// create a scope for the loop
		scope = scope.addScope();
		stmt.setSymbol(scope);

		// note expression
		rememberExpression(scope, stmt);

		// proceed with body statements
		annotateStatements(scope, stmt.getBody());
	}

	/**
	 * re-annotates a ForEachStatemetn (second pass)
	 * 
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException
	 * @throws WrongModifierException
	 * @throws ShadowedIdentifierException
	 * @throws DuplicateIdentifierException
	 */
	protected void reannotateForEachStatement(Scope scope, ForEachStatement stmt)
			throws InvalidIdentifierException, DuplicateIdentifierException,
			ShadowedIdentifierException, WrongModifierException {
		Symbol symType = exprAnnotator.annotateExpression(scope, stmt
				.getValues());
		assert (symType.hasType(SymbolType.ARRAY_TYPE));
		ArrayType symArrayType = (ArrayType) symType;

		Symbol symElemType = symArrayType.getComponentType();
		scope.addVariable(stmt.getElement(), symElemType, PUBLIC_MODIFIER);
	}

	/**
	 * annotates a scope statement
	 * 
	 * @param scope
	 *            the scope, where the scope statement lies in
	 * @param stmt
	 *            the scope statement
	 * @throws DuplicateIdentifierException
	 * @throws ShadowedIdentifierException
	 * @throws InvalidIdentifierException
	 * @throws WrongModifierException
	 */
	protected void annotateScopeStatement(Scope scope, ScopeStatement stmt)
			throws DuplicateIdentifierException, ShadowedIdentifierException,
			InvalidIdentifierException, WrongModifierException {
		// create a new scope
		scope = scope.addScope();
		stmt.setSymbol(scope);
		// proceed with body statements
		annotateStatements(scope, stmt.getBody());
	}
	
	/**
	 * annotates a SetStatement
	 * @param scope
	 * @param stmt
	 */
	protected void annotateSetStatement(Scope scope, SetStatement stmt) {
		rememberExpression(scope, stmt);
	}
	
	/**
	 * re-annotates a SetStatement (second pass)
	 * @param scope
	 * @param stmt
	 * @throws InvalidIdentifierException 
	 */
	protected void reannotateSetStatement(Scope scope, SetStatement stmt) throws InvalidIdentifierException {
		Symbol symLValueType = exprAnnotator.annotateExpression(scope, stmt.getLValue());
		Symbol symRValueType = exprAnnotator.annotateExpression(scope, stmt.getRValue());
		//TODO check type...
		
	}
}
