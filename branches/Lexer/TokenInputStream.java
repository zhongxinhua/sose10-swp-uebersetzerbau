import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.regex.*;

public class TokenInputStream implements Closeable {
	
	protected static final int MAX_LINE_LENGTH = 1 << 16;
	
	protected static final Map<TokenType, Pattern> tokenTypes;
	static {
		tokenTypes = new EnumMap<TokenType, Pattern>(TokenType.class);
		
		tokenTypes.put(TokenType.LINEBREAK,        Pattern.compile("^(?:\\n\\r|\\n|\\r)"));
		tokenTypes.put(TokenType.WHITESPACE,       Pattern.compile("^[\0- ]"));
		
		tokenTypes.put(TokenType.CIRCUMFLEX,       Pattern.compile("^\\^"));
		tokenTypes.put(TokenType.EXCLAMATION_MARK, Pattern.compile("^!"));
		tokenTypes.put(TokenType.QUOT,             Pattern.compile("^\""));
		tokenTypes.put(TokenType.DOLLAR,           Pattern.compile("^\\$"));
		tokenTypes.put(TokenType.AMPERSAND,        Pattern.compile("^&"));
		tokenTypes.put(TokenType.SLASH,            Pattern.compile("^/"));
		tokenTypes.put(TokenType.PAREN_OPEN,       Pattern.compile("^\\("));
		tokenTypes.put(TokenType.PAREN_CLOSE,      Pattern.compile("^\\)"));
		tokenTypes.put(TokenType.EQUALS,           Pattern.compile("^="));
		tokenTypes.put(TokenType.QUESTION_MARK,    Pattern.compile("^\\?"));
		tokenTypes.put(TokenType.BRACE_OPEN,       Pattern.compile("^\\{"));
		tokenTypes.put(TokenType.BRACE_CLOSE,      Pattern.compile("^\\}"));
		tokenTypes.put(TokenType.BRACKET_OPEN,     Pattern.compile("^\\["));
		tokenTypes.put(TokenType.BRACKET_CLOSE,    Pattern.compile("^\\]"));
		tokenTypes.put(TokenType.BACKSLASH,        Pattern.compile("^\\\\"));
		tokenTypes.put(TokenType.AT,               Pattern.compile("^@"));
		tokenTypes.put(TokenType.PLUS,             Pattern.compile("^\\+"));
		tokenTypes.put(TokenType.TIMES,            Pattern.compile("^\\*"));
		tokenTypes.put(TokenType.TILDE,            Pattern.compile("^\\~"));
		tokenTypes.put(TokenType.HASH,             Pattern.compile("^#"));
		tokenTypes.put(TokenType.APOS,             Pattern.compile("^'"));
		tokenTypes.put(TokenType.LESS_THAN,        Pattern.compile("^<"));
		tokenTypes.put(TokenType.GREATER_THAN,     Pattern.compile("^>"));
		tokenTypes.put(TokenType.PIPE,             Pattern.compile("^\\|"));
		tokenTypes.put(TokenType.COMMA,            Pattern.compile("^,"));
		tokenTypes.put(TokenType.SEMICOLON,        Pattern.compile("^;"));
		tokenTypes.put(TokenType.DOT,              Pattern.compile("^\\."));
		tokenTypes.put(TokenType.COLON,            Pattern.compile("^:"));
		tokenTypes.put(TokenType.MINUS,            Pattern.compile("^-"));
		
		tokenTypes.put(TokenType.REAL,             Pattern.compile("^[0-9]*\\.[0-9](?:[eE][+-]?[0-9]+)?"));
		tokenTypes.put(TokenType.INTEGER,          Pattern.compile("^[0-9]+"));
		tokenTypes.put(TokenType.IDENTIFIER,       Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*"));
	}
	
	protected final PushbackReader in;
	protected final String name;
	protected int currentIndex;
	
	public TokenInputStream() {
		this(System.in, "-", 1);
	}
	
	public TokenInputStream(InputStream in, String name) {
		this(in, name, 0);
	}
	
	public TokenInputStream(Reader in, String name) {
		this(in, name, 0);
	}
	
	public TokenInputStream(InputStream in, String name, int currentIndex) {
		this(new InputStreamReader(in), name, currentIndex);
	}
	
	public TokenInputStream(Reader in, String name, int currentIndex) {
		this.in = new PushbackReader(in, MAX_LINE_LENGTH);
		this.name = name;
		this.currentIndex = currentIndex;
	}
	
	public void close() throws IOException {
		in.close();
	}
	
	protected final CharBuffer buffer = CharBuffer.allocate(MAX_LINE_LENGTH);
	public Token readNextToken() throws IOException {
		final int oldIndex = currentIndex;
		
		int position = 0;
		for(;;) {
			int read = in.read(buffer.array(), position, MAX_LINE_LENGTH - position);
			if(read < 0) {
				break;
			}
			position += read;
		}
		if(position == 0) {
			return new Token(TokenType.EOF, this, null, oldIndex, currentIndex);
		}
		
		TokenType longestType = TokenType.ERROR;
		int longestLength = 0;
		
		for(final Map.Entry<TokenType,Pattern> tokenType : tokenTypes.entrySet()) {
			final Matcher matcher = tokenType.getValue().matcher(buffer);
			if(matcher.find()) {
				int len = matcher.end();
				if(len > longestLength) {
					longestLength = len;
					longestType = tokenType.getKey();
				}
			}
		}
		
		in.unread(buffer.array(), longestLength, position-longestLength);
		currentIndex += longestLength;
		
		final String string = String.valueOf(buffer.array(), 0, longestLength);
		return new Token(longestType, this, string, oldIndex, currentIndex);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
