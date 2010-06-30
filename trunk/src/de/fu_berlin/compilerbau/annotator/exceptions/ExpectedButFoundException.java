package de.fu_berlin.compilerbau.annotator.exceptions;

import de.fu_berlin.compilerbau.util.StreamPosition;

@SuppressWarnings("serial")
public class ExpectedButFoundException extends LocatableException {
	public ExpectedButFoundException(StreamPosition position,  String expected, String found) {
		super(position, "'"+expected+"' expected, but '"+found+"' found.");
	}
}
