public class BasicToken {
	
	/**
	 * inclusive
	 */
	public final int start;
	
	/**
	 * exclusive (!)
	 */
	public final int end;
	
	/**
	 * stream the token was read from
	 */
	public final TokenInputStream stream;
	
	public BasicToken(TokenInputStream stream, int start, int end) {
		this.stream = stream;
		this.start = start;
		this.end = end;
	}
	
	public <Dest extends Appendable> Dest appendTo(Dest dest) throws java.io.IOException {
		dest.append("[" + start + "," + end + "[");
		dest.append(stream.toString());
		dest.append(";");
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
}
