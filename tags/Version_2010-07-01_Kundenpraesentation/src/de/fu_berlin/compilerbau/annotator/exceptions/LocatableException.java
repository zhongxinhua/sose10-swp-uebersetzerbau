package de.fu_berlin.compilerbau.annotator.exceptions;

import de.fu_berlin.compilerbau.util.StreamPosition;

@SuppressWarnings("serial")
public class LocatableException extends Exception {
	private StreamPosition position;
	public LocatableException(StreamPosition position, String msg) {
		super("(Line: "+position.getLine()+"; Column: "+position.getColumn()+") => "+msg);
		this.position = position;
	}
	public StreamPosition getPosition() {
		return position;
	}
}
