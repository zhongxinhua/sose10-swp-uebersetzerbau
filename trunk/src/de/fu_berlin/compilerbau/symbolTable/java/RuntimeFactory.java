package de.fu_berlin.compilerbau.symbolTable.java;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.fu_berlin.compilerbau.symbolTable.GetModifier;
import de.fu_berlin.compilerbau.symbolTable.Modifier;
import de.fu_berlin.compilerbau.symbolTable.Package;
import de.fu_berlin.compilerbau.symbolTable.Runtime;
import de.fu_berlin.compilerbau.symbolTable.Symbol;
import de.fu_berlin.compilerbau.symbolTable.SymbolType;
import de.fu_berlin.compilerbau.symbolTable.exceptions.DuplicateIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.ShadowedIdentifierException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.SymbolTableException;
import de.fu_berlin.compilerbau.symbolTable.exceptions.WrongModifierException;
import de.fu_berlin.compilerbau.util.Pair;
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
	 * @throws IOException 
	 * @throws WrongModifierException 
	 * @throws ShadowedIdentifierException 
	 * @throws DuplicateIdentifierException 
	 */
	public static Runtime newRuntime(Iterator<Map.Entry<PositionString,PositionString>> imports,
			URL[] classpath, URL rtJar) throws IOException {
		RuntimeImpl result = new RuntimeImpl();
		
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
					String pkgName = fileName.replaceAll("/", "\\.");
					pkgName = pkgName.substring(0, pkgName.length() - DOT_CLASS.length());
					Class<?> clazz = loader.findClass(pkgName);
					populateFromNativeClass(result, pkgName, clazz); // indentation too big ...
				}
			} catch(ClassNotFoundException e) {
				throw new RuntimeException("A class in a JAR could not be loaded.", e);
			} catch(SymbolTableException e) {
				throw new RuntimeException("Internal error.", e);
			}
			
			jarInputStream.close();
		}
		
		return result;
	}

	private static void populateFromNativeClass(RuntimeImpl result, String pkgName, Class<?> clazz)
			throws SymbolTableException {
		PositionString pkgLookupName = new PositionString(pkgName, PositionBean.ZERO);
		Symbol pkgSymbol = result.getQualifiedSymbol(pkgLookupName, SymbolType.PACKAGE);
		if(pkgSymbol == null) {
			// class private, protected oder private Klasse
			return;
		}
		Package pkg = (Package)pkgSymbol;

		final Symbol extends_;
		Class<?> superclass = clazz.getSuperclass();
		if(superclass != null) {
			PositionString name = new PositionString(superclass.getName(), PositionBean.ZERO);
			extends_ = result.getUniqualifiedSymbol(name, SymbolType.CLASS);
		} else {
			extends_ = null;
		}
		
		List<Symbol> implements_ = null; // TODO: Interfaces raussuchen

		PositionString classLookupName = new PositionString(pkgName, PositionBean.ZERO);
		Modifier clazzModifiers = new NativeModifier(clazz.getModifiers());
		de.fu_berlin.compilerbau.symbolTable.Class clazzSymbol =
				pkg.addClass(classLookupName, extends_, implements_.iterator(), clazzModifiers);
		
		for(Field field : clazz.getDeclaredFields()) {
			NativeModifier fieldModifiers = new NativeModifier(field.getModifiers());
			PositionString name = new PositionString(field.getName(), PositionBean.ZERO);
			Symbol type = result.getUniqualifiedSymbol(name, SymbolType.CLASS_OR_INTERFACE);
			clazzSymbol.addMember(name, type, fieldModifiers);
		}
		
		// TODO: Methoden und Ctors hinzufügen
	}
	
}
