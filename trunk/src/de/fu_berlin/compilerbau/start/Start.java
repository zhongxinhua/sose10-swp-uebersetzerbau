/*
 *  Copyright (C) 2010  The software project Compilerbau SS10 group
 *                                               (swp+uebersetzerbau@lists.spline.inf.fu-berlin.de)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.fu_berlin.compilerbau.start;

/**
 * @author rene
 * @author stefan
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import de.fu_berlin.compilerbau.annotator.Annotator;
import de.fu_berlin.compilerbau.builder.Builder;
import de.fu_berlin.compilerbau.builder.Director;
import de.fu_berlin.compilerbau.builder.JavaBuilder;
import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;
import de.fu_berlin.compilerbau.directoryWriter.PhysicalDirectoryWriter;
import de.fu_berlin.compilerbau.directoryWriter.ZipDirectoryWriter;
import de.fu_berlin.compilerbau.dom.DomCreator;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.java.RuntimeFactory;
import de.fu_berlin.compilerbau.util.ErrorHandler;

/**
 * Starts the Compiler process
 */
class Start {
	
	/** The name of the generated JAR file */
	private static final String JAR_NAME = "compiler.jar";
	
	//Markus
	//protected static final String RT_JAR = "testfiles/small_runtime.jar";
	//Rene
	protected static final String RT_JAR = "/usr/lib/jvm/java-6-openjdk/jre/lib/rt.jar";
	
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

	public static void main(final String[] args) throws Throwable {
		
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
		
		final Start start = new Start(classpath, source, destPath);
		try {
			final boolean result = start.compile();
			if(result) {
				System.err.println("\nCompiled.");
				System.exit(0);
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		System.err.println("\nCompiling failed.");
		System.exit(1);
		
	}

	protected final String classpath, source, destPath;
	protected final Thread currentThread = Thread.currentThread();
	protected Runtime runtime;
	
	protected Start(String classpath, String source, String destPath) {
		this.classpath = classpath;
		this.source = source;
		this.destPath = destPath;
		rtLoaderThread.start();
	}

	protected final Thread rtLoaderThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				final LinkedList<URL> cp = new LinkedList<URL>();
				if(classpath != null) {
					for(String s : classpath.split(":")) {
						cp.add(new File(s).toURI().toURL());
					}
				}
				final URL[] cpJARs = cp.toArray(new URL[cp.size()]);
				final URL rtJAR = new File(RT_JAR).toURI().toURL();
				Start.this.runtime = RuntimeFactory.newRuntime(cpJARs, rtJAR);
			} catch (Exception e) {
				// Start.this.currentThread.stop(new InterruptedException());
				Start.this.currentThread.interrupt();
				throw new RuntimeException(e);
			}
		}
		
	}, "rt-loader");
	
	protected boolean compile() throws Throwable {
		
		ErrorHandler.init(true); //true==show debug information
		
		final Reader in;
		final ReadableByteChannel channel;
		if(source == null || "-".equals(source)) {
			channel = Channels.newChannel(System.in);
		} else {
			channel = new FileInputStream(source).getChannel();
		}
		in = Channels.newReader(channel, "UTF-8");
		
		//XML parsen
		DomCreator.init(in);
		DomNode node = DomCreator.createDOM();
		if(node == null) {
			return false; // input was empty
		}
		
		//Syntaxbaum erstellen
		AbstractSyntaxTree stree = new AbstractSyntaxTree(node);
		
		// Ausgabe von Fehlern und ggf. Abbruch
		if(ErrorHandler.errorOccured()) {
			System.err.println(ErrorHandler.getErrorCount() + " error(s) and " +
					ErrorHandler.getWarningCount() + " warning(s) occured. Cannot compile!");
			
			// Schlecht!
			// sollte evtl. der ErrorHandler uebernehmen
			return false;
		} else if(ErrorHandler.getWarningCount() > 0) {
			System.err.println(ErrorHandler.getWarningCount() + " warning(s) occured.");
		}
		
		rtLoaderThread.join();
		if(runtime == null) {
			return false;
		}
		
		// TODO: imports
		
		new Annotator(runtime, stree);
		
		//Ausgabe, wenn keine Fehler aufgetreten sind
		DirectoryWriter directoryWriter;
		if("-".equals(destPath)) {
			directoryWriter = new ZipDirectoryWriter(System.out);
		} else {
			directoryWriter = new PhysicalDirectoryWriter(new File(destPath));
		}
		
		Builder builder = new JavaBuilder(stree, classpath);
		Director.build(builder, directoryWriter);
		directoryWriter.close();
		
		File[] fileList = getJavaFiles(destPath);
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		
		Iterable<? extends JavaFileObject> compilationUnit =
			fileManager.getJavaFileObjectsFromFiles(Arrays.asList(fileList));
		compiler.getTask(null, fileManager, null, null, null, compilationUnit).call();
		
		fileManager.close();
		
		return true;
		
	}
	
	private File[] getJavaFiles(String rootPath) {
		Vector<File> fileList = new Vector<File>();
		File dir = new File(rootPath);
		File[] files = dir.listFiles();
		for(File f : files) {
			if(f.isFile() && f.getName().endsWith(".java")) fileList.add(f);
			else if(f.isDirectory()) {	    	
				fileList.addAll(Arrays.asList(getJavaFiles(f.getAbsolutePath())));
			}
		}
		
		return (File[]) fileList.toArray(new File[fileList.size()]);	
	}
	
}
