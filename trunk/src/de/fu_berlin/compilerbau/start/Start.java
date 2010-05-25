package de.fu_berlin.compilerbau.start;

/**
 * @author rene, stefan
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

import de.fu_berlin.compilerbau.builder.Builder;
import de.fu_berlin.compilerbau.builder.Director;
import de.fu_berlin.compilerbau.builder.JavaBuilder;
import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;
import de.fu_berlin.compilerbau.directoryWriter.PhysicalDirectoryWriter;
import de.fu_berlin.compilerbau.directoryWriter.ZipDirectoryWriter;
import de.fu_berlin.compilerbau.dom.DomCreator;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * Starts the Compiler process
 */
class Start {
	
	/** The name of the generated JAR file */
	private static final String JAR_NAME = "compiler.jar";
	
	/**
	 * Does not return.
	 * @param isError
	 */
	private static void printHelp(final boolean isError) {
		PrintStream out = isError ? System.err : System.out;
		
		out.println("java -jar " + JAR_NAME + " [options] [source]");
		out.println("Options:");
		out.println("  -source      -f    Source file (\"-\" means STDIN)");
		out.println("  -dest        -d    Destination folder (\"-\" means STDOUT [zipped])");
		out.println("  -classpath   -cp   Classpath");
		
		System.exit(isError ? 1 : 0);
	}

	public static void main(final String[] args) {
		ErrorHandler.init();
		
		final Iterator<String> i = Arrays.asList(args).iterator();
		
		String classpath = null;
		String source = null;
		String destPath = null;
		
		while(i.hasNext()) { 
			String arg = i.next();
			if("-cp".equals(arg) || "-classpath".equals(arg)) {
				if(i.hasNext()) {
					classpath = i.next();
				} else {
					System.err.println(arg + " awaits an argument!");
					System.exit(1);
				}
			} else if("-f".equals(arg) || "-source".equals(arg)) {
				if(i.hasNext()) {
					source = i.next();
				} else {
					System.err.println(arg + " awaits an argument!");
					System.exit(1);
				}
			} else if("-d".equals(arg) || "-dest".equals(arg)) {
				if(i.hasNext()) {
					destPath = i.next();					
				} else {
					System.err.println(arg + " awaits an argument!");
					System.exit(1);
				}
			} else if("-h".equals(arg) || "-help".equals(arg)) {
				printHelp(false);
			} else if(
					source != null ||       // source may not be re-set
					arg.length() == 0 ||    // empty file name??
					arg.charAt(0) != '-' || // "-" means stdin
					i.hasNext()             // there may not be any further arguments!
			) {
				printHelp(true);
			} else {
				source = arg;
			}
		}
		
		final Reader in;
		try {
			if(source == null || "-".equals(source)) {
				in = new InputStreamReader(System.in);
			} else {
				in = new FileReader(source);
			}
		} catch(FileNotFoundException e) {
			throw new RuntimeException("Could not open source.", e);
		}
		
		try {
			DomCreator.init(in);
		} catch(IOException e) {
			throw new RuntimeException("Could not initialize DOM.", e);
		}
		DomNode node = DomCreator.createDOM();
		AbstractSyntaxTree stree = new AbstractSyntaxTree(node);
		
		DirectoryWriter directoryWriter;
		if("-".equals(destPath)) {
			try {
				directoryWriter = new ZipDirectoryWriter(System.out);
			} catch(IOException e) {
				throw new RuntimeException("Could not open STDOUT for writing.");
			}
		} else {
			directoryWriter = new PhysicalDirectoryWriter(new File(destPath));
		}
		
		Builder builder = new JavaBuilder(stree, classpath);
		try {
			Director.build(builder, directoryWriter);
		} catch(IOException e) {
			throw new RuntimeException("Could not write compiled code.", e);
		}
		try {
			directoryWriter.close();
		} catch(IOException e) {
			throw new RuntimeException("Could not close output.", e);
		}
		
		if(ErrorHandler.errorOccured()) {
			System.err.println(ErrorHandler.getErrorCount() + " error(s) and " +
					ErrorHandler.getWarningCount() + " warning(s) occured. Cannot compile!");
			System.exit(1);
		} else if(ErrorHandler.getWarningCount() > 0) {
			System.err.println(ErrorHandler.getWarningCount() + " warning(s) occured.");
		}
	}
	
}
