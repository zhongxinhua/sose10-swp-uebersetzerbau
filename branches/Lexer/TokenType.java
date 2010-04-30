enum TokenType {
	LINEBREAK,        // \n\r | \n | \r
	WHITESPACE,       // \0 - \x20
	
	CIRCUMFLEX,       // ^
	EXCLAMATION_MARK, // !
	QUOT,             // "
	DOLLAR,           // $
	AMPERSAND,        // &
	SLASH,            // /
	PAREN_OPEN,       // (
	PAREN_CLOSE,      // )
	EQUALS,           // =
	QUESTION_MARK,    // ?
	BRACE_OPEN,       // {
	BRACE_CLOSE,      // }
	BRACKET_OPEN,     // [
	BRACKET_CLOSE,    // ]
	BACKSLASH,        // \
	AT,               // @
	PLUS,             // +
	TIMES,            // *
	TILDE,            // ~
	HASH,             // #
	APOS,             // '
	LESS_THAN,        // <
	GREATER_THAN,     // >
	PIPE,             // |
	COMMA,            // ,
	SEMICOLON,        // ;
	DOT,              // .
	COLON,            // :
	MINUS,            // -
	
	REAL,             // [0-9]*\.[0-9]([eE][+-]?[0-9]+)?
	INTEGER,          // [0-9]+
	IDENTIFIER,       // [_a-zA-Z][_a-zA-Z0-9]*
	
	EOF,
	ERROR
}
