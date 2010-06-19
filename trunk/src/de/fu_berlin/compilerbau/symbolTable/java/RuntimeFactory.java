package de.fu_berlin.compilerbau.symbolTable.java;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.fu_berlin.compilerbau.symbolTable.ClassOrInterface;
import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolContainer;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.Variable;
import de.fu_berlin.compilerbau.symbolTable.exceptions.InvalidIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.SymbolTableException;
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

	private RuntimeFactory() {
		// void
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
	public static Runtime newRuntime(Iterator<Map.Entry<PositionString,PositionString>> imports,
			URL[] classpath, URL rtJar) throws IOException {
		
		final RuntimeImpl result = new RuntimeImpl();
		result.setNameManglingEnabled(false);
		
		final PackageLoader loader = new PackageLoader(rtJar, classpath);
		try {
			for(java.lang.Package pkg : loader.getPackages()) {
				PositionString name = new PositionString(pkg.getName(), PositionBean.ZERO);
				result.addPackage(name, PACKAGE_MODIFIER);
			}
		} catch(SymbolTableException e) {
			throw new RuntimeException("Could not populate Runtime with Java's packages.", e);
		}

		for(URL url : loader.getURLs()) {
			URLConnection openConnection = url.openConnection();
			openConnection.connect();
			InputStream inputStream = openConnection.getInputStream();
			JarInputStream jarInputStream = new JarInputStream(inputStream);

			try {
				for(JarEntry entry; (entry = jarInputStream.getNextJarEntry()) != null;) {
					String fileName = entry.getName();
					if(!fileName.endsWith(DOT_CLASS)) {
						continue;
					}
					String className = fileName.replaceAll("/", "\\.");
					className = className.substring(0, className.length() - DOT_CLASS.length());
					Class<?> clazz = Class.forName(className, false, loader);
					String pkgName = className.substring(0, className.lastIndexOf('.'));
					populateFromNativeClass(result, pkgName, className, clazz); // indentation too big ...
				}
			} catch(ClassNotFoundException e) {
				throw new RuntimeException("A class in a JAR could not be loaded.", e);
			} catch(SymbolTableException e) {
				throw new RuntimeException("Internal error.", e);
			}
			
			jarInputStream.close();
		}
		
		List<SymbolContainer> unqualifiedSymbols = result.qualifyAllSymbols();
		if(unqualifiedSymbols != null && !unqualifiedSymbols.isEmpty()) {
			System.err.println("Runtime contains unqualified symbol(s) in");
			for(SymbolContainer symbol : unqualifiedSymbols) {
				System.err.println("\t" + symbol);
			}
			throw new RuntimeException("Runtime contains unqualified symbols!");
		}
		
		result.setNameManglingEnabled(true);
		return result;
	}

	protected static final class ArgumentIterator implements Iterator<Variable> {
		
		private static final Modifier ARGUMENT_MODIFIER =
				GetModifier.getModifier(Visibility.DEFAULT, false, false, false);
		
		private final Runtime rt;
		private final Symbol[] types;
		
		public ArgumentIterator(Runtime rt, Class<?>[] classes) {
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
			PositionString arg = new PositionString("arg" + i, PositionBean.ZERO);
			VariableImpl result;
			try {
				result = new VariableImpl(rt, null, arg, types[i], ARGUMENT_MODIFIER);
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
	
	private static void populateFromNativeClass(final Runtime rt, String pkgName,
			String className, Class<?> clazz) throws SymbolTableException {
		
		PositionString pkgLookupName = new PositionString(pkgName, PositionBean.ZERO);
		Symbol pkgSymbol = rt.getQualifiedSymbol(pkgLookupName, SymbolType.PACKAGE);
		if(pkgSymbol == null) {
			// package private, protected oder private Klasse
			return;
		}
		Package pkg = (Package)pkgSymbol;

		Symbol[] ifSymbols = javaToCompilerTypes(rt, clazz.getInterfaces());

		PositionString classLookupName = new PositionString(className, PositionBean.ZERO);
		Modifier clazzModifiers = new NativeModifier(clazz.getModifiers());
		Iterator<Symbol> implements_ = Arrays.asList(ifSymbols).iterator();
		ClassOrInterface coiSymbol;
		if(!clazz.isInterface()) {
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
				final NativeModifier modifiers = new NativeModifier(field.getModifiers());
				final PositionString name = new PositionString(field.getName(), PositionBean.ZERO);
				final Symbol type = javaToCompilerType(rt, field.getType());
				clazzSymbol.addMember(name, type, modifiers);
			}
			
			for(Constructor<?> ctor : clazz.getDeclaredConstructors()) {
				final NativeModifier modifiers = new NativeModifier(ctor.getModifiers());
				final Iterator<Variable> parameters = new ArgumentIterator(rt, ctor.getParameterTypes());
				clazzSymbol.addConstructor(PositionBean.ZERO, parameters, modifiers);
			}
		} else {
			coiSymbol = pkg.addInterface(classLookupName, implements_, clazzModifiers);
		}
		
		for(Method method : clazz.getDeclaredMethods()) {
			final NativeModifier modifiers = new NativeModifier(method.getModifiers());
			final Iterator<Variable> parameters = new ArgumentIterator(rt, method.getParameterTypes());
			final Symbol resultType = javaToCompilerType(rt, method.getReturnType());
			final PositionString name = new PositionString(method.getName(), PositionBean.ZERO);
			coiSymbol.addMethod(name, resultType, parameters, modifiers);
		}
		
	}
	
	/**
	 * Java's type names have to be put in our {@link Symbol} schema.
	 * This method translates a {@link Class} into a unqualified symbol.
	 * @param rt
	 * @param type
	 * @return
	 */
	static Symbol javaToCompilerType(Runtime rt, Class<?> type) {
		
		final PositionString name = new PositionString(type.getName(), PositionBean.ZERO);
		
		Symbol result;
		if(type == Void.TYPE) {
			result = rt.getVoid();
		} else if(type.isPrimitive()) {
			result = rt.getPrimitiveType(type);
		} else if(type.isArray()) {
			result = rt.getArrayType(type);
		} else if(type.isInterface()) {
			result = rt.getUnqualifiedSymbol(name, SymbolType.INTERFACE);
		} else {
			result = rt.getUnqualifiedSymbol(name, SymbolType.CLASS);
		}
		
		return result;
		
	}
	
	/**
	 * @see #javaToCompilerType(Runtime, Class)
	 */
	static Symbol[] javaToCompilerTypes(Runtime rt, Class<?> type[]) {
		Symbol[] result = new Symbol[type.length];
		for(int i = 0; i < type.length; ++i) {
			result[i] = javaToCompilerType(rt, type[i]);
		}
		return result;
	}
	
}
