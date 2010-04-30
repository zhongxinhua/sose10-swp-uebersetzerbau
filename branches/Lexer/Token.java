public class Token extends BasicToken {
	
	public final TokenType type;
	
	/**
	 * The read string.
	 */
	public final CharSequence string;
	
	public Token(TokenType type, TokenInputStream stream, CharSequence string, int start, int end) {
		super(stream, start, end);
		this.type = type;
		this.string = string;
	}
	
	public <Dest extends Appendable> Dest appendTo(Dest dest) throws java.io.IOException {
		dest.append(type.toString());
		super.appendTo(dest);
		dest.append("<");
		dest.append(string);
		dest.append(">");
		return dest;
	}
	
	/**
	 * @throws IllegalArgumentException Left and right weren't consecutive in the same {@link #stream}.
	 */
	public static Token concat(TokenType type, Token left, Token right) throws IllegalArgumentException {
		if(left.stream != right.stream) {
			throw new IllegalArgumentException("Left stream is not the right stream.");
		} else if(left.start > right.start || left.end > right.end) {
			throw new IllegalArgumentException("Left and right overlap.");
		} else if(left.end != right.start) {
			throw new IllegalArgumentException("Left end was not right start.");
		} else {
			return new Token(type, left.stream, left.string.toString()+right.string, left.start, right.end);
		}
	}
	
	public boolean isEnd() {
		return type == TokenType.EOF;
	}
	
	public boolean isError() {
		return type == TokenType.ERROR;
	}
	
}
