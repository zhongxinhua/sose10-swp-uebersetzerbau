package de.fu_berlin.compilerbau.builder;

import de.fu_berlin.compilerbau.parser.Module;

public class JavaBuilder extends Builder {

	@Override
	public void buildClass() {		
	}

	@Override
	public void buildModule() {
		Module root = _astree.getRoot();
		_code.append("package " + root.getName().toString() + ";");
	}

}
