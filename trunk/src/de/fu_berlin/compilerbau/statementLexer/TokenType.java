package de.fu_berlin.compilerbau.statementLexer;

public enum TokenType {
	EOF,
	
	ERROR,

	DOT,

	PAREN_OPEN,

	PAREN_CLOSE,

	BRACKET_OPEN,

	BRACKET_CLOSE,
	
	BRACE_OPEN,

	BRACE_CLOSE,

	INCR,

	DECR,

	NOT,

	NEW,

	NULL,

	TIMES,

	DIVIDES,

	MOD,

	PLUS,

	MINUS,

	LE,

	GE,

	LT,

	GT,

	NE,

	EQ,

	ASSIGN,

	BIT_AND,

	BIT_XOR,

	BIT_OR,

	AND,

	OR,

	COMMA,

	ID, 
	INT, 
	FLOAT, 
	STRING
}
