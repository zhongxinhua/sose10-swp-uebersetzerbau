package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 */

import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.Type;

public class JavaBuilder extends Builder {

	@Override
	public void buildModule() {
		Module root = _astree.getRoot();
		_code.append("package " + root.getName().toString() + ";\n");
		
		for(Class currClass : root.getClasses()) {
			buildClass(currClass);
		}
	}
	
	@Override
	protected void buildClass(Class theclass) {
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
	protected void buildDecleration(DeclarationStatement decl) {
		Type type = decl.getType();
		if(type == Type.TYPE_STRING) {
			_code.append("\tString " + decl.getName().toString() + ";\n");
		}
		else if (type == Type.TYPE_INT) {
			_code.append("\tint " + decl.getName().toString() + ";\n");
		}
		else if (type == Type.TYPE_FLOAT) {
			_code.append("\tfloat " + decl.getName().toString() + ";\n");
		}
	}
	
	@Override
	protected void buildFunction(Function func) {
		_code.append("public ");
		
		Type type = func.getReturnType();
		if(type == Type.TYPE_STRING) {
			_code.append("String ");
		}
		else if (type == Type.TYPE_INT) {
			_code.append("int ");
		}
		else if (type == Type.TYPE_FLOAT) {
			_code.append("float ");
		}
		
		_code.append(func.getName().toString() + " (");
		// Argumente der Funktion
		_code.append("");
		_code.append(") {\n");
		
		_code.append("}\n");
	}

}
