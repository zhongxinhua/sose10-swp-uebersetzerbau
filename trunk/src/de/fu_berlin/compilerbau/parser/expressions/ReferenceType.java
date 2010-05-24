package de.fu_berlin.compilerbau.parser.expressions;

import de.fu_berlin.compilerbau.parser.ClassOrInterface;

public class ReferenceType extends Type {
	public static Type get(String type) {
		//TODO NEIN! schau nach ob Klasse mit entspr. Namen in der Symboltabelle existiert
		return null;
	}	
	ClassOrInterface classRef;
}