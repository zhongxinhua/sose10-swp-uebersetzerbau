public class Test {
	public static void main(String[] args) throws Throwable {
		BasicTokenInputStream strm = new BasicTokenInputStream();
		for(;;) {
			Token token = strm.readNextToken();
			System.out.println(token);
			if(token.isEnd() || token.isError()) {
				break;
			}
		}
	}
}
