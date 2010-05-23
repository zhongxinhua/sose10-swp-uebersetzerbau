package de.fu_berlin.compilerbau.builder;

/**
 * @author stefan
 */

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.Function;

public abstract class Builder {
	
	protected AbstractSyntaxTree _astree;
	protected StringBuffer _code;
	
	public Builder() {
		_code = new StringBuffer();
	}
	
	public void setAbstractSyntaxTree(AbstractSyntaxTree val) {
		_astree = val;
	}

	public abstract void buildModule();
	protected abstract void buildClass(Class theclass);
	protected abstract void buildDecleration(DeclarationStatement decl);
	protected abstract void buildFunction(Function func);
	
	public StringBuffer getCode() { return _code; }
}
