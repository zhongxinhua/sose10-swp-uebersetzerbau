class Token {
	
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
	
}
