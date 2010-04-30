public class Token {
	
	public final TokenType type;
	
	/**
	 * inclusive
	 */
	public final int start;
	
	/**
	 * exclusive (!)
	 */
	public final int end;
	
	/**
	 * The read string.
	 */
	public final CharSequence string;
	
	/**
	 * stream the token was read from
	 */
	public final BasicTokenInputStream stream;
	
	public Token(TokenType type, BasicTokenInputStream stream, CharSequence string, int start, int end) {
		this.type = type;
		this.stream = stream;
		this.string = string;
		this.start = start;
		this.end = end;
	}
	
	public <Dest extends Appendable> Dest appendTo(Dest dest) throws java.io.IOException {
		dest.append(type.toString());
		dest.append("[" + start + "," + end + "[");
		dest.append(stream.toString());
		dest.append(";<");
		dest.append(string);
		dest.append(">");
		return dest;
	}
	
	@Override
	public String toString() {
		try {
			return appendTo(new StringBuilder()).toString();
		} catch(java.io.IOException e) {
			throw new RuntimeException("A StringBuilder threw an IOException.");
		}
	}
	
	public boolean isEnd() {
		return type == TokenType.EOF;
	}
	
	public boolean isError() {
		return type == TokenType.ERROR;
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
	
}
