package de.fu_berlin.compilerbau.parser;

import java.util.*;

public class AbstractSyntaxTree {

}

class SymbolTable {
  SymbolTable parent;
  //List<SymbolTable> children;
  //List<Instance> instances;
}

class Module {
  String name;
  //List<Type> imports;
  List<ClassOrInterface> declarations;
}

class ClassOrInterface {
  String name;
  List<Interface> interfaces;
}

class Class extends ClassOrInterface {
  Class parent;
  List<Attribute> attributes;
  List<Function> functions;
}

class Interface extends ClassOrInterface {
  List<AbstractFunction> functions;
}

class Attribute {
  String name;
  Type type;
  Expression value;
}

class Type {
  static final Type TYPE_STRING = new Type() { {this.name = "string"; } };
  static final Type TYPE_INT    = new Type() { {this.name = "int"; } };
  static final Type TYPE_FLOAT  = new Type() { {this.name = "float"; } };
  String name;
}

class Reference extends Type {
  Class classRef;
}

class AbstractFunction {
  //ClassOrInterface parent;
  String name;
  List<Attribute> parameters;
}

class Function extends AbstractFunction {
  List<Statement> body;
}

//-------------------------------------------------------------------//
abstract class Statement {}
class BreakStatement extends Statement {} 
class ContinueStatement extends Statement {}
class ReturnStatement extends Statement {
  Expression value;
} 
 
class DoStatement extends Statement {
  Expression test;
  List<Statement> body;
} 

class DeclarationStatement extends Statement {
  Type type;
  int dimension;
  boolean isStatic;
  boolean isFinal;
  String name;
  Expression value;
}

class AssignStatement extends Statement {
  LeftHandside lvalue;
  Expression rvalue;
}

class CallStatement extends Statement {
  FunctionCall call;
} 

class ScopeStatement extends Statement {
  SymbolTable symbolTable;
  List<Statement> body;
} 

class ChooseStatement extends Statement {
  List<Case> cases;
}

class Case {
  Expression test;
  List<Statement> body;
}

//-------------------------------------------------------------------//
abstract class Expression {}

class Operation extends Expression { //a+b
  BinaryOperator operator;
  Expression left;
  Expression right; 
}

class UnaryOperation extends Expression { //!a
  UnaryOperator operator;
  Expression term; 
}

enum UnaryOperator {
  NOT, PLUS, MINUS, PREINC, PREDEC, POSTINC, POSTDEC
}

enum BinaryOperator {
  ADD, MINUS, MUL, DIV, MOD, GREATERTHAN, LESSTHAN, 
  GREATEREQUAL, LESSEQUAL, NOTEQUAL, EQUAL, AND, OR, XOR
}

class FunctionCall extends Expression { //a(b,c)
  Function function;
  List<Expression> actualParams;
}

class LeftHandside extends Expression {}

//class ExpressionName extends LeftHandside {
//  List<String> names;
//}

//class FieldAccess extends LeftHandside { 
//  [this | super | (Expression) | Literal | Name | Name [Expression]](.[FunctionCall | Name | Name [Expression]])*        
//}

//class ArrayAccess extends LeftHandside {
//  Expression index;
  //ExpressionName [ Expression ]
  //PrimaryNoNewArray [ Expression ]
//}