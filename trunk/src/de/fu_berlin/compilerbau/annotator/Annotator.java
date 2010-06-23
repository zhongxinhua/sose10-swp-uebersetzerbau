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
import de.fu_berlin.compilerbau.parser.Statement;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Method;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Scope;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ContainerSymbolsException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Visibility;

public class Annotator {
	protected Runtime runtime;
	protected final static Modifier PUBLIC_MODIFIER = GetModifier.getModifier(
			Visibility.PUBLIC, false, false, false);

	public Annotator(Runtime runtime, AbstractSyntaxTree ast) {
		this.runtime = runtime;
		// TODO imports übergeben
		try {
			annotateModule(ast.getRoot());
			runtime.qualifyAllSymbols();
		} catch (ContainerSymbolsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongModifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void annotateModule(Module module)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// Module
		Package symModule = runtime.addPackage(module.getName(), PUBLIC_MODIFIER);
		module.setSymbol(symModule);

		// Interfaces
		for (Interface iface : module.getInterfaces())
			annotateInterface(symModule, iface);

		// Classes
		for (de.fu_berlin.compilerbau.parser.Class clazz : module.getClasses())
			annotateClass(symModule, clazz);
	}

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

	protected void annotateClass(Package pack,
			de.fu_berlin.compilerbau.parser.Class class_)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// füge Elternklasse ein
		Symbol symParent = runtime.tryGetQualifiedSymbol(class_.getSuper());

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

		// füge Attribute ein
		for (DeclarationStatement decl : class_.getDeclarations())
			annotateDeclaration(symClass, decl);

		// füge Funktionen ein
		for (Function func : class_.getFunctions())
			annotateFunction(symClass, func);
	}

	protected void annotateDeclaration(
			de.fu_berlin.compilerbau.symbolTable.Class class_,
			DeclarationStatement decl) throws InvalidIdentifierException,
			ContainerSymbolsException, WrongModifierException {
		Symbol type = runtime.tryGetQualifiedSymbol(decl.getType());
		class_.addMember(decl.getName(), type, PUBLIC_MODIFIER);
	}

	protected void annotateFunction(ClassOrInterface owner, Function function)
			throws ContainerSymbolsException, WrongModifierException,
			InvalidIdentifierException {
		// ermittle Rückgabewert
		Symbol symReturnType = runtime.tryGetQualifiedSymbol(function
				.getReturnType());

		// ermittle Parameter
		List<Variable> parameters = new LinkedList<Variable>();
		for (DeclarationStatement decl : function.getArguments()) {
			Symbol type = runtime.tryGetQualifiedSymbol(decl.getType());
			// if(decl.isArray())
			// type =
			// TODO: arrays!!!
			Modifier modifier = GetModifier.getModifier(Visibility.PUBLIC, decl
					.isStatic(), decl.isFinal(), false);
			parameters.add(runtime.getNewVariableForParameter(decl.getName(), type,
					modifier));
		}

		// füge Funktion ein
		Method symMethod = owner.addMethod(function.getName(), symReturnType,
				parameters.iterator(), GetModifier.getModifier(Visibility.PUBLIC,
						function.isStatic(), function.isStatic(), false));
		function.setSymbol(symMethod);

		// füge Statements ein
		annotateStatements(symMethod.getScope(), function.getBody());
	}

	protected void annotateStatements(Scope scope, List<Statement> body) {
				Scope symScope;
				for(Statement stmt : body) {
					if(stmt instanceof BreakStatement) { /*nothing...*/ }
					else if(stmt instanceof ContinueStatement)	{ /*nothing...*/ }
					
					else if(stmt instanceof CallStatement)	{ /*nothing...*/ }
					else if(stmt instanceof ReturnStatement)	{ /*nothing...*/ }
					else if(stmt instanceof DeclarationStatement)	{ /*nothing...*/ }
					
					else if(stmt instanceof ChooseStatement)	{ 
						stmt.setSymbol(symScope = scope.addScope());
						ChooseStatement cstmt = (ChooseStatement) stmt;
						for(Case case_ : cstmt.getCases()) {
							Scope caseScope = symScope.addScope();
							//TODO test expression
							annotateStatements(caseScope, case_.getBody());
						}
					}
					else if(stmt instanceof DoStatement)	{ 
						stmt.setSymbol(symScope = scope.addScope());
						//TODO test expression
						annotateStatements(symScope, ((DoStatement) stmt).getBody());
					}
					else if(stmt instanceof ForEachStatement)	{ 
						stmt.setSymbol(symScope = scope.addScope());
						
					}					
					
					else if(stmt instanceof ScopeStatement)	{
						stmt.setSymbol(symScope = scope.addScope());
						annotateStatements(symScope, ((ScopeStatement) stmt).getBody());
					}
				}
	}
}
