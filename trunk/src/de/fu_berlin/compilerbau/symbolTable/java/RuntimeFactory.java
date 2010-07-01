/*
 *  Copyright (C) 2010  René Kijewski  (rene.<surname>@fu-berlin.de)
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

package de.fu_berlin.compilerbau.symbolTable.java;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static java.lang.reflect.Modifier.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.SymbolTableException;
import de.fu_berlin.compilerbau.util.ErrorHandler;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;
import de.fu_berlin.compilerbau.util.Visibility;

/**
 * Returns a new {@link Runtime}.
 * @author kijewski
 */
public class RuntimeFactory {
	
	private static final Modifier PACKAGE_MODIFIER =
		GetModifier.getModifier(Visibility.PUBLIC, false, false, false);
	private static final String DOT_CLASS = ".class";
	
	protected static final Map<String,PositionString> positionStrings = new HashMap<String,PositionString>();

	private RuntimeFactory() {
		// void
	}
	
	protected static final class LoaderThread implements Runnable {

		protected final JarInputStream jarInputStream;
		protected final PackageLoader loader;
		protected final BlockingDeque<Callable<ClassOrInterface>> queue;
		protected final RuntimeImpl runtime;
		protected final AtomicInteger semaphore;
		protected final Object notifier;
		
		public LoaderThread(JarInputStream jarInputStream, PackageLoader loader,
				BlockingDeque<Callable<ClassOrInterface>> queue, RuntimeImpl runtime,
				AtomicInteger semaphore, Object notifier) {
			this.jarInputStream = jarInputStream;
			this.loader = loader;
			this.queue = queue;
			this.runtime = runtime;
			this.semaphore = semaphore;
			this.notifier = notifier;
		}

		@Override
		public void run() {
			try {
				for(;;) {
					final JarEntry entry;
					synchronized (jarInputStream) {
						entry = jarInputStream.getNextJarEntry();
					}
					if(entry == null) {
						break;
					}

					final String fileName = entry.getName();
					if(!fileName.endsWith(DOT_CLASS)) {
						continue;
					}
					
					String className = fileName.replaceAll("/", "\\.");
					className = className.substring(0, className.length() - DOT_CLASS.length());
					
					if(!className.startsWith("java.") && !className.startsWith("javax.")) {
						continue; // skip
					}
					
					final Class<?> clazz = Class.forName(className, false, loader);
					if(clazz.isSynthetic() || clazz.isAnonymousClass()) {
						continue;
					}
					
					if((clazz.getModifiers() & (PROTECTED|PUBLIC)) == 0) {
						continue; // skip package-private and private classes
					}
					
					// TODO: Bahandlung für clazz.isMemberClass();

					final String pkgName = className.substring(0, className.lastIndexOf('.'));
					final String simplyfiedClassName = className.substring(pkgName.length() + 1);
					
					final Callable<ClassOrInterface> call = new Callable<ClassOrInterface>() {

						@Override
						public ClassOrInterface call() throws SymbolTableException {
							ErrorHandler.debugMsg(null, "Reading: " + pkgName + "/" + simplyfiedClassName);
							final ClassOrInterface result =
								populateFromNativeClass(runtime, pkgName, simplyfiedClassName, clazz);
							return result;
						}
						
					};
					
					queue.put(call);
					synchronized (notifier) {
						notifier.notify();
					}
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			} finally {
				semaphore.incrementAndGet();
			}
		}

	}
	
	/**
	 * Returns a new {@link Runtime runtime}.
	 * @param imports Imports to be known [(package name, alias)].
	 * 	«Alias» may be null. «Package name» may end with a ".*", i.e. static imports may not be
	 * 	declared as such explicitly.
	 * @param classpath JARs of this classpath
	 * @param rtJar The JAR containing the JRE, e.g. "/usr/lib/jvm/java-6-openjdk/jre/lib/rt.jar".
	 * @throws IOException Something in the underlying IO system went wrong!
	 */
	public static Runtime newRuntime(URL[] classpath, URL rtJar) throws IOException {
				final long start = System.currentTimeMillis();
		
		final RuntimeImpl result = new RuntimeImpl();
		result.setNameManglingEnabled(false);

		int packageCount = 0;
		final PackageLoader loader = new PackageLoader(rtJar, classpath);
		try {
			for(java.lang.Package pkg : loader.getPackages()) {
				String pkgName = pkg.getName();
				PositionString name = new PositionString(pkgName, PositionBean.ZERO);
				result.addPackage(name, PACKAGE_MODIFIER);
				positionStrings.put(pkgName, name);
				++packageCount;
			}
		} catch(SymbolTableException e) {
			throw new RuntimeException("Could not populate Runtime with Java's packages.", e);
		}
		
		final Thread[] loaderThreads = new Thread[java.lang.Runtime.getRuntime().availableProcessors() + 1];
		for(URL url : loader.getURLs()) {
			final URLConnection openConnection = url.openConnection();
			openConnection.connect();
			final InputStream inputStream = openConnection.getInputStream();
			final JarInputStream jarInputStream = new JarInputStream(inputStream);
			final AtomicInteger semaphore = new AtomicInteger(-loaderThreads.length);
			final Object notifier = new Object();
			
			final BlockingDeque<Callable<ClassOrInterface>> queue = new LinkedBlockingDeque<
					Callable<ClassOrInterface>>(30);
			for(int i = 0; i < loaderThreads.length; ++i) {
				loaderThreads[i] = new Thread(new LoaderThread(jarInputStream, loader, queue,
						result, semaphore, notifier), "LoaderThread-" + (i+1));
			}
			
			for(Thread thread : loaderThreads) {
				thread.start();
			}

			try {
				outer:for(;;) {
					final Callable<ClassOrInterface> job;
					inner:for(;;) {
						final Callable<ClassOrInterface> job_ = queue.poll();
						if(job_ != null) {
							job = job_;
							break;
						} else {
							if(semaphore.get() >= 0) {
								break outer;
							} else {
								synchronized (notifier) {
									notifier.wait(0, 10 * 1000);
									continue inner;
								}
							}
						}
					}
					job.call();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			jarInputStream.close();
		}

		try {
			result.qualifyAllSymbols();
		} catch (SymbolTableException e) {
			throw new RuntimeException(e);
		}
		
		try {
			result.useImport(new PositionString("java.lang.*", PositionBean.ZERO), null);
		} catch (SymbolTableException e) {
			throw new RuntimeException(e);
		}
		
		//built-in functions
		try {
			//TODO
			de.fu_berlin.compilerbau.symbolTable.Class predefined = result.undefinedScope.addClass(new PositionString("Predefined", PositionBean.ZERO), null, null, null);
			de.fu_berlin.compilerbau.symbolTable.Method read =
				predefined.addMethod(new PositionString("read", PositionBean.ZERO), result.getPrimitiveType(int.class), null, null);
			List<Variable> printParams = new LinkedList<Variable>();
			printParams.add(result.getNewVariableForParameter(new PositionString("str", PositionBean.ZERO), result.getQualifiedSymbol(new PositionString("java.lang.String", PositionBean.ZERO)), null));
			de.fu_berlin.compilerbau.symbolTable.Method print =
				predefined.addMethod(new PositionString("print", PositionBean.ZERO), result.getVoid(), printParams.iterator(), null);

			result.globalScope.useStaticImport(read, null);
			result.globalScope.useStaticImport(print, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		final long end = System.currentTimeMillis();
		
		ErrorHandler.debugMsg(null, "Read: " + packageCount + " packages");
		ErrorHandler.debugMsg(null, "Read: " + classesNum + " classes");
		ErrorHandler.debugMsg(null, "Read: " + interfacesNum + " interfaces");
		ErrorHandler.debugMsg(null, "Read: " + methodsNum + " methods");
		ErrorHandler.debugMsg(null, "Read: " + constructorsNum + " constructors");
		ErrorHandler.debugMsg(null, "Read: " + fieldsNum + " fields");
		ErrorHandler.debugMsg(null, "Read runtime in " + (end-start)/1000f + " seconds.");
		
		result.setNameManglingEnabled(true);
		return result;
		
	}

	protected static final class ArgumentIterator implements Iterator<Variable> {
		
		private static final Modifier ARGUMENT_MODIFIER =
				GetModifier.getModifier(Visibility.DEFAULT, false, false, false);
		
		private final Runtime rt;
		private final Symbol[] types;
		
		public ArgumentIterator(Runtime rt, Class<?>[] classes) throws InvalidIdentifierException {
			this.rt = rt;
			this.types = javaToCompilerTypes(rt, classes);
		}
		
		private int i = 0;
		
		@Override
		public boolean hasNext() {
			return i < types.length;
		}
		
		@Override
		public Variable next() {
			final int item = i++;
			String argName = "arg" + item;
			PositionString arg = positionStrings.get(argName);
			if(arg == null) {
				arg = new PositionString("arg" + item, PositionBean.ZERO);
				positionStrings.put(argName, arg);
			}
			VariableImpl result;
			try {
				result = new VariableImpl(rt, null, arg, types[item], ARGUMENT_MODIFIER);
			} catch (InvalidIdentifierException e) {
				throw new RuntimeException(e);
			}
			return result;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	protected static int classesNum, interfacesNum, methodsNum, constructorsNum, fieldsNum;
	
	private static ClassOrInterface populateFromNativeClass(final Runtime rt, String pkgName,
			String className, Class<?> clazz) throws SymbolTableException {
		
		PositionString pkgLookupName = positionStrings.get(pkgName);
		if(pkgLookupName == null) {
			pkgLookupName = new PositionString(pkgName, PositionBean.ZERO);
			positionStrings.put(pkgName, pkgLookupName);
		}
		Symbol pkgSymbol = rt.getQualifiedSymbol(pkgLookupName, SymbolType.PACKAGE);
		if(pkgSymbol == null) {
			// package private, protected oder private Klasse
			return null;
		}
		Package pkg = (Package)pkgSymbol;

		Symbol[] ifSymbols = javaToCompilerTypes(rt, clazz.getInterfaces());
		
		PositionString classLookupName = positionStrings.get(className);
		if(classLookupName == null) {
			classLookupName = new PositionString(className, PositionBean.ZERO);
			positionStrings.put(className, classLookupName);
		}
		Modifier clazzModifiers = new NativeModifier(clazz.getModifiers());
		Iterator<Symbol> implements_ = Arrays.asList(ifSymbols).iterator();
		ClassOrInterface coiSymbol;
		if(!clazz.isInterface()) {
			++classesNum;
			
			final Symbol extends_;
			Class<?> superclass = clazz.getSuperclass();
			if(superclass != null) {
				extends_ = javaToCompilerType(rt, superclass);
			} else {
				extends_ = null;
			}
			
			final de.fu_berlin.compilerbau.symbolTable.Class clazzSymbol =
				pkg.addClass(classLookupName, extends_, implements_, clazzModifiers);
			coiSymbol = clazzSymbol; 
			
			for(Field field : clazz.getDeclaredFields()) {
				if(field.isSynthetic()) {
					continue;
				}
				++fieldsNum;
				
				final int javaModifiers = field.getModifiers();
				if((javaModifiers & (PROTECTED|PUBLIC)) == 0) {
					continue; // skip package-private and private fields
				}
				
				final NativeModifier modifiers = new NativeModifier(javaModifiers);
				final String fieldName = field.getName();
				PositionString name = positionStrings.get(fieldName);
				if(name == null) {
					name = new PositionString(field.getName(), PositionBean.ZERO);
					positionStrings.put(fieldName, name);
				}
				final Symbol type = javaToCompilerType(rt, field.getType());
				clazzSymbol.addMember(name, type, modifiers);
			}
			
			for(Constructor<?> ctor : clazz.getDeclaredConstructors()) {
				if(ctor.isSynthetic()) {
					continue;
				}
				++constructorsNum;
				
				final int javaModifiers = ctor.getModifiers();
				if((javaModifiers & (PROTECTED|PUBLIC)) == 0) {
					continue; // skip package-private and private fields
				}
				
				final NativeModifier modifiers = new NativeModifier(javaModifiers);
				final Iterator<Variable> parameters = new ArgumentIterator(rt, ctor.getParameterTypes());
				clazzSymbol.addConstructor(PositionBean.ZERO, parameters, modifiers);
			}
		} else {
			++interfacesNum;
			coiSymbol = pkg.addInterface(classLookupName, implements_, clazzModifiers);
		}
		
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.isSynthetic()) {
				continue;
			}
			++methodsNum;
			
			final int javaModifiers = method.getModifiers();
			if((javaModifiers & (PROTECTED|PUBLIC)) == 0) {
				continue; // skip package-private and private methods
			}
			
			final NativeModifier modifiers = new NativeModifier(javaModifiers);
			final Iterator<Variable> parameters = new ArgumentIterator(rt, method.getParameterTypes());
			final Symbol resultType = javaToCompilerType(rt, method.getReturnType());
			String methodName = method.getName();
			PositionString name = positionStrings.get(methodName);
			if(name == null) {
				name = new PositionString(method.getName(), PositionBean.ZERO);
				positionStrings.put(methodName, name);
			}
			
			coiSymbol.addMethod(name, resultType, parameters, modifiers);
		}

		return coiSymbol;
		
	}
	
	/**
	 * Java's type names have to be put in our {@link Symbol} schema.
	 * This method translates a {@link Class} into a unqualified symbol.
	 * @param rt
	 * @param type
	 * @return
	 * @throws InvalidIdentifierException 
	 */
	static Symbol javaToCompilerType(Runtime rt, Class<?> type) throws InvalidIdentifierException {
		
		String typeName = type.getName();
		PositionString name = positionStrings.get(typeName);
		if(name == null) {
			name = new PositionString(type.getName(), PositionBean.ZERO);
			positionStrings.put(typeName, name);
		}
		
		final Symbol result;
		if(type == Void.TYPE) {
			result = rt.getVoid();
		} else if(type.isPrimitive()) {
			result = rt.getPrimitiveType(type);
		} else if(type.isArray()) {
			result = rt.getArrayType(type);
		} else if(type.isInterface()) {
			result = rt.tryGetQualifiedSymbol(name, SymbolType.INTERFACE);
		} else {
			result = rt.tryGetQualifiedSymbol(name, SymbolType.CLASS);
		}
		
		return result;
		
	}
	
	/**
	 * @throws InvalidIdentifierException 
	 * @see #javaToCompilerType(Runtime, Class)
	 */
	static Symbol[] javaToCompilerTypes(Runtime rt, Class<?> type[]) throws InvalidIdentifierException {
		Symbol[] result = new Symbol[type.length];
		for(int i = 0; i < type.length; ++i) {
			result[i] = javaToCompilerType(rt, type[i]);
		}
		return result;
	}
	
}
