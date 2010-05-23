package de.fu_berlin.compilerbau.builder;

import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;

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
	
	public abstract void buildClass();
	
	public StringBuffer getCode() { return _code; }
}
