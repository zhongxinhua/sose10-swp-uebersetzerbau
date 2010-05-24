package de.fu_berlin.compilerbau.util;

import de.fu_berlin.compilerbau.dom.DomNode;

public class ErrorHandler {
	private static int errorCount;
	private static int warningCount;
	
	public static void init() {
		errorCount = 0;
		warningCount = 0;
	}
	
	public static void error(DomNode node, String msg) {
		System.err.println("Error: "+msg);
		errorCount++;
	}
	
	public static void warning(DomNode node, String msg) {
		System.err.println("Warning: "+msg);
		warningCount++;
	}
	
	public static boolean errorOccured() {
		return errorCount>0;
	}
	
	public static int getErrorCount() {
		return errorCount;
	}
	
	public static int getWarningCount() {
		return errorCount;
	}
}
