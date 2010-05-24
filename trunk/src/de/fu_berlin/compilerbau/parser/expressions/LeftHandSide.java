package de.fu_berlin.compilerbau.parser.expressions;


public interface LeftHandSide {
	LHSTail getNext();
}

interface LHSTail {
	LHSTail getNext();
}

class SuperHead extends Expression implements LeftHandSide {
	@Override
	public LHSTail getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printTree(int deep) {
		// TODO Auto-generated method stub
		
	}
}

class ThisHead extends Expression implements LeftHandSide {
	@Override
	public LHSTail getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printTree(int deep) {
		// TODO Auto-generated method stub
		
	}
}

class FunctionCallTail extends Expression  implements LeftHandSide,LHSTail {
	FunctionCall call;
	@Override
	public LHSTail getNext() {
		return null;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void printTree(int deep) {
		// TODO Auto-generated method stub
		
	}
}

class NameTail extends Expression implements LeftHandSide,LHSTail {
	String name;
	Expression index; //can be null
	@Override
	public LHSTail getNext() {
		return null;
	}
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void printTree(int deep) {
		// TODO Auto-generated method stub
		
	}	
}
//class ExpressionName extends LeftHandside {
//List<String> names;
//}

//class FieldAccess extends LeftHandside { 
//Literal = "abc" oder 123 oder 1.23

//[this | super | (Expression / Literal) | Name | Name [Expression]]
//(.[FunctionCall | Name | Name [Expression]])*

//}

//class ArrayAccess extends LeftHandside {
//Expression index;
//ExpressionName [ Expression ]
//PrimaryNoNewArray [ Expression ]
//}