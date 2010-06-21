package de.fu_berlin.compilerbau.annotator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.ExtendsStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementsStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.Visibility;

public abstract class Annotator {
	protected Runtime runtime;
	protected final static Modifier PUBLIC_MODIFIER = 
		GetModifier.getModifier(Visibility.PUBLIC, false, false, false); 
	
	public Annotator(Runtime runtime, AbstractSyntaxTree ast) {
		this.runtime = runtime;
		annotateModule(ast.getRoot());
	}
	
	protected void annotateModule(Module module) {
		//Module
		Package symModule = null;
		try {
			symModule = runtime.addPackage(
							module.getName(), 
							PUBLIC_MODIFIER
						);
			module.setSymbol(symModule);
		} catch (DuplicateIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShadowedIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongModifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//Interfaces
		for(Interface iface : module.getInterfaces())
			annotateInterface(symModule, iface);
			
		//Classes
		for(de.fu_berlin.compilerbau.parser.Class clazz : module.getClasses())
			annotateClass(symModule, clazz);	
		
	}
	
	protected void annotateInterface(Package pack, Interface iface) {	
		//füge Erweiterungen ein
		List<Symbol> extends_ = new LinkedList<Symbol>();
		for(ExtendsStatement ex : iface.getExtensions()) {
			Symbol sym = runtime.getUnqualifiedSymbol(ex.getName());
			ex.setSymbol(sym);
			extends_.add(sym);
		}
		
		//füge Interface ein
		de.fu_berlin.compilerbau.symbolTable.Interface symInterface = null;
		try {
			symInterface = pack.addInterface(iface.getName(), extends_.iterator(), PUBLIC_MODIFIER);
			iface.setSymbol(symInterface);
		} catch (DuplicateIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShadowedIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongModifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(symInterface == null)
			return;
		
		//füge Funktionen ein
		for(Function func : iface.getFunctions())
			annotateFunction(symInterface, func);
		
	}
	
	protected void annotateClass(Package pack, de.fu_berlin.compilerbau.parser.Class class_) {
		//füge Elternklasse ein
		Symbol symParent = runtime.getUnqualifiedSymbol(class_.getSuper());
		
		//füge Interfaces ein
		List<Symbol> implements_ = new LinkedList<Symbol>();
		for(ImplementsStatement impl : class_.getImplementations()) {
			Symbol sym = runtime.getUnqualifiedSymbol(impl.getName());
			impl.setSymbol(sym);
			implements_.add(sym);
		}
		
		//füge Klasse ein
		de.fu_berlin.compilerbau.symbolTable.Class symClass = null;
		
		try {
			symClass = pack.addClass(class_.getName(), symParent, implements_.iterator(), PUBLIC_MODIFIER);
		} catch (DuplicateIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShadowedIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongModifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(symClass == null)
			return;
		
		//füge Attribute ein
		for(DeclarationStatement decl : class_.getDeclarations())
			annotateDeclaration(symClass, decl);
		
		//füge Funktionen ein
		for(Function func : class_.getFunctions())
			annotateFunction(symClass, func);
	}
	
	protected abstract void annotateDeclaration(de.fu_berlin.compilerbau.symbolTable.Class class_, DeclarationStatement decl);
	
	protected void annotateFunction(ClassOrInterface owner, Function function)  {
		/*//ermittle Rückgabewert
		Symbol symReturnType = runtime.getUnqualifiedSymbol(function.getReturnType());
		
		//ermittle Parameter
		List<Variable> parameters = new LinkedList<Variable>();
		for(DeclarationStatement decl : function.getArguments()) {
			PositionString type = decl.getType();
			parameters.add(runtime.getNewVariableForParameter(decl.getName(), , ));
		}
		
		//füge Funktion ein
		owner.addMethod(
			function.getName(), 
			symReturnType, 
			parameters, 
			GetModifier.getModifier(Visibility.PUBLIC, function.isStatic(), function.isStatic(), false)
		);*/
	}
}
