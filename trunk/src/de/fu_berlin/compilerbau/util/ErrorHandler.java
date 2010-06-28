package de.fu_berlin.compilerbau.util;

import java.io.PrintStream;

public class ErrorHandler {
	private static int errorCount;
	private static int warningCount;
	private static boolean showDebug;
	
	public static void init(boolean showDebugMessages) {
		errorCount = 0;
		warningCount = 0;
		showDebug = showDebugMessages;
	}
	
	public static void error(StreamPosition position, String msg) {
		msg(System.err, position, "Error", msg);
		errorCount++;
	}
	
	public static void warning(StreamPosition position, String msg) {
		msg(System.err, position, "Warning", msg);
		warningCount++;
	}
	
	public static void debugMsg(StreamPosition position, String msg) {
		if(showDebug)
			msg(System.out, position, "Debug", msg);
	}
	
	private static void msg(PrintStream dstOut, StreamPosition position, String prefix, String msg) {
		dstOut.print(prefix);
		if(position != null)
			dstOut.print(" @ ("+position.getLine()+" : "+position.getColumn()+")");		
		dstOut.print(": ");
		dstOut.println(msg);
	}
	
	public static boolean errorOccured() {
		return errorCount>0;
	}
	
	public static int getErrorCount() {
		return errorCount;
	}
	
	public static int getWarningCount() {
		return warningCount;
	}
}
