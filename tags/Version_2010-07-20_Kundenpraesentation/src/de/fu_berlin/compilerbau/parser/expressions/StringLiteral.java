package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein StringLiteral stellt im abstrakten Syntaxbaum einen 
 * elementaren Stringausdruck dar.
 * @author Markus
 */
@SuppressWarnings("serial")
public class StringLiteral extends Literal {
	CharSequence value;
	
	public StringLiteral(CharSequence value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "string("+value+")";
	}
	//BEGIN get-Methoden f�r Builder
	public CharSequence getValue(){
		return value;
	}
	//END get-Methoden f�r Builder
}
