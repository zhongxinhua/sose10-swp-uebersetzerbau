package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein FloatLiteral stellt im abstrakten Syntaxbaum einen 
 * Ganzzahlwert dar.
 * @author Markus
 */
@SuppressWarnings("serial")
public class IntegerLiteral extends Literal {
	private int value;
	
	public IntegerLiteral(Number value) {
		this.value = value.intValue();
	}
	
	@Override
	public String toString() {
		return "integer("+Integer.toString(value)+")";
	}
	//BEGIN get-Methoden f�r Builder
	public int getValue(){
		return value;
	}
	//END get-Methoden f�r Builder
}
