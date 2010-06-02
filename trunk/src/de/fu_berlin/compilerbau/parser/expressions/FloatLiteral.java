package de.fu_berlin.compilerbau.parser.expressions;

/**
 * Ein FloatLiteral stellt im abstrakten Syntaxbaum einen 
 * FlieÃŸkommaausdruck dar.
 * @author Markus
 */
public class FloatLiteral extends Literal {
	private float value;
	
	public FloatLiteral(Number value) {
		this.value = value.floatValue();
	}

	@Override
	public Type getType() {
		return Type.FLOAT;
	}
	
	@Override
	public String toString() {
		return "float("+Float.toString(value)+")";
	}
	//BEGIN get-Methoden für Builder
	public float getValue(){
		return value;
	}
	//END get-Methoden für Builder
}
