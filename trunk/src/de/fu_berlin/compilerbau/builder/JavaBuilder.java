package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 */

import java.io.IOException;

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
import de.fu_berlin.compilerbau.parser.expressions.Type;

public class JavaBuilder extends Builder {
	
	public JavaBuilder(AbstractSyntaxTree astree, String classpath) {
		super(astree, classpath);
	}

	@Override
	protected void buildClass(Class theclass) throws IOException {
		Module root = _astree.getRoot();
		_code.append("package " + root.getName().toString() + ";\n");
		
		_code.append("class " + theclass.getName().toString() + " {\n");
		
		for(DeclarationStatement decl : theclass.getDeclerations()) {
			buildDecleration(decl);
		}
		
		for(Function func : theclass.getFunctions()) {
			buildFunction(func);
		}
		
		_code.append("}\n");
	}
	
	@Override
	protected void buildDecleration(DeclarationStatement decl) throws IOException {
		Type type = decl.getType();
		if(type == Type.STRING) {
			_code.append("\tString " + decl.getName().toString() + ";\n");
		}
		else if (type == Type.INTEGER) {
			_code.append("\tint " + decl.getName().toString() + ";\n");
		}
		else if (type == Type.FLOAT) {
			_code.append("\tfloat " + decl.getName().toString() + ";\n");
		}
	}
	
	@Override
	protected void buildFunction(Function func) throws IOException {
		_code.append("public ");
		
		Type type = func.getReturnType();
		if(type == Type.STRING) {
			_code.append("String ");
		}
		else if (type == Type.INTEGER) {
			_code.append("int ");
		}
		else if (type == Type.FLOAT) {
			_code.append("float ");
		}
		
		_code.append(func.getName().toString() + " (");
		// Argumente der Funktion
		_code.append("");
		_code.append(") {\n");
		
		_code.append("}\n");
	}

	@Override
	protected void buildAssignStatement(SetStatement obj) throws IOException {
		// TODO Auto-generated method stub
		
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
	protected void buildContinueStatement(ContinueStatement obj) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildDeclarationStatement(DeclarationStatement obj) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildDoStatement(DoStatement obj) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildImplementStatement(ImplementStatement obj) throws IOException {
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

}
