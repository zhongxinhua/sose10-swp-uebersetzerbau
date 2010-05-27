package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein StringLiteral stellt im abstrakten Syntaxbaum einen 
 * elementaren Stringausdruck dar.
 * @author Markus
 */
public class StringLiteral extends Literal {
	CharSequence value;
	
	public StringLiteral(CharSequence value) {
		this.value = value;
	}

	@Override
	public Type getType() {
		return Type.STRING;
	}

	@Override
	public String toString() {
		return "string("+value+")";
	}
}
