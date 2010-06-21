package de.fu_berlin.compilerbau.annotator.java;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fu_berlin.compilerbau.annotator.Annotator;
import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.ExtendsStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementsStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.symbolTable.Class;
import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Visibility;

public class AnnotatorImpl extends Annotator {	
	public AnnotatorImpl(Runtime runtime, AbstractSyntaxTree ast) {
		//TODO parameterübergabe ungünstig, da runtime eine andere Implementierung sein könnte...
		super(runtime, ast);
	}

	@Override
	protected void annotateDeclaration(Class class1, DeclarationStatement decl) {
		// TODO Auto-generated method stub
		
	}
}
