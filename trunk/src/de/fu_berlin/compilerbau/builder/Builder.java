package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 */

import java.io.IOException;
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
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementStatement;
import de.fu_berlin.compilerbau.parser.ImportStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.ReturnStatement;
import de.fu_berlin.compilerbau.parser.ScopeStatement;
import de.fu_berlin.compilerbau.parser.SetStatement;
import de.fu_berlin.compilerbau.parser.StaticStatement;

public abstract class Builder {
	
	protected final AbstractSyntaxTree _astree;
	protected final String[] _classpath;
	
	protected Appendable _code = null;
	
	public Builder(AbstractSyntaxTree astree, String classpath) {
		_astree = astree;
		
		if(classpath == null || classpath.isEmpty()) {
			_classpath = new String[0];
		} else {
			_classpath = Pattern.compile(":").split(classpath);
		}
	}

	protected abstract void buildClass(Class theclass) throws IOException;
	protected abstract void buildDecleration(DeclarationStatement decl) throws IOException;
	protected abstract void buildFunction(Function func) throws IOException;

	protected abstract void buildAssignStatement(SetStatement obj) throws IOException;
	protected abstract void buildBreakStatement(BreakStatement obj) throws IOException;
	protected abstract void buildCallStatement(CallStatement obj) throws IOException;
	protected abstract void buildCase(Case obj) throws IOException;
	protected abstract void buildChooseStatement(ChooseStatement obj) throws IOException;
	protected abstract void buildContinueStatement(ContinueStatement obj) throws IOException;
	protected abstract void buildDeclarationStatement(DeclarationStatement obj) throws IOException;
	protected abstract void buildDoStatement(DoStatement obj) throws IOException;
	protected abstract void buildImplementStatement(ImplementStatement obj) throws IOException;
	protected abstract void buildImportStatement(ImportStatement obj) throws IOException;
	protected abstract void buildInterface(Interface obj) throws IOException;
	protected abstract void buildModule(Module obj) throws IOException;
	protected abstract void buildReturnStatement(ReturnStatement obj) throws IOException;
	protected abstract void buildScopeStatement(ScopeStatement obj) throws IOException;
	protected abstract void buildStaticStatement(StaticStatement obj) throws IOException;

	public void setCode(Appendable code) {
		_code = code;
	}
	
	public Appendable getCode() {
		return _code;
	}
	
}
