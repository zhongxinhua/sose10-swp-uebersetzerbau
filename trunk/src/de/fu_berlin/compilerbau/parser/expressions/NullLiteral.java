package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein FloatLiteral stellt im abstrakten Syntaxbaum einen 
 * den Wert NULL dar.
 * @author Markus
 */
public class NullLiteral extends Literal {
	public static final NullLiteral NULL = new NullLiteral();
	
	@Override
	public String toString() {
		return "null";
	}
}
