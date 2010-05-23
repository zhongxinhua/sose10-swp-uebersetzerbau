package de.fu_berlin.compilerbau.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Printer {

	static FileWriter out;
	
	static public void init(File outputFile) {
		try {
			out = new FileWriter(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public void append(String line) {
		try {
			out.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
