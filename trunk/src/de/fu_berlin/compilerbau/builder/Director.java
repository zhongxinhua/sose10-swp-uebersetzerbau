package de.fu_berlin.compilerbau.builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Director {
	Builder _builder;
	
	public void setBuilder(Builder builder) {
		_builder = builder;
	}
	
	public void construct() {
		_builder.buildModule();
		_builder.buildClass();
	}
	
	public void print (File out) {
		try {
			FileWriter fw = new FileWriter(out);
			fw.append(_builder.getCode().toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
