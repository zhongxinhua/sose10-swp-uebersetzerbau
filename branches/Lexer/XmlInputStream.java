import java.io.*;
import java.util.*;

public class XmlInputStream implements Closeable {
	
	protected static enum States {
		ERROR,
		
		OSComment0,
		OSComment1,
		OSComment2,
		OSComment3,
		OSComment,
		OSPIStringApos,
		OSPIStringQuot,
		OSProcessingInstruction0,
		OSProcessingInstruction1,
		OSProcessingInstruction,
		OuterScopeLT,
		OuterScope,
		XmlOuterLT,
		XmlOuter,
		XmlTag,
		XOClose0,
		XOClose,
		XOComment0,
		XOComment1,
		XOComment2,
		XOComment3,
		XOComment,
		XOPIStringApos,
		XOPIStringQuot,
		XOProcessingInstruction0,
		XOProcessingInstruction1,
		XOProcessingInstruction,
		XTAttr0,
		XTAttr1,
		XTAttr2,
		XTAttrApos,
		XTAttrQuot,
		XTNET
	}
	
	protected static Map<States, Map<TokenType, States>> deltas;
	protected static void putDeltaToAllBut(States src, TokenType exception, States dest) {
		Map<TokenType, States> map = deltas.get(States.OSProcessingInstruction1);
		for(TokenType token : TokenType.values()) {
			if(!token.equals(exception)) {
				map.put(token, dest);
			}
		}
	}
	static {
		deltas = new EnumMap<States, Map<TokenType, States>>(States.class);
		for(States state : States.values()) {
			deltas.put(state, new EnumMap<TokenType, States>(TokenType.class));
		}
		
		deltas.get(States.OuterScope).put(TokenType.LESS_THAN, States.OuterScopeLT);
		deltas.get(States.OuterScope).put(TokenType.WHITESPACE, States.OuterScope);

		/* http://www.w3.org/TR/xml11/#sec-comments */
		deltas.get(States.OuterScopeLT).put(TokenType.EXCLAMATION_MARK, States.OSComment0);
		deltas.get(States.OSComment0).put(TokenType.MINUS, States.OSComment1);
		deltas.get(States.OSComment1).put(TokenType.MINUS, States.OSComment);
		deltas.get(States.OSComment).put(TokenType.MINUS, States.OSComment2);
		putDeltaToAllBut(States.OSComment2, TokenType.MINUS, States.OSComment);
		deltas.get(States.OSComment2).put(TokenType.MINUS, States.OSComment3);
		deltas.get(States.OSComment3).put(TokenType.GREATER_THAN, States.OuterScope);

		/* http://www.w3.org/TR/xml11/#sec-pi */
		deltas.get(States.OuterScopeLT).put(TokenType.QUESTION_MARK, States.OSProcessingInstruction0);
		deltas.get(States.OSProcessingInstruction0).put(TokenType.IDENTIFIER, States.OSProcessingInstruction);
		deltas.get(States.OSProcessingInstruction).put(TokenType.WHITESPACE, States.OSProcessingInstruction);
		deltas.get(States.OSProcessingInstruction).put(TokenType.QUESTION_MARK, States.OSProcessingInstruction1);
		deltas.get(States.OSProcessingInstruction1).put(TokenType.GREATER_THAN, States.OuterScope);
		deltas.get(States.OSProcessingInstruction1).put(TokenType.QUESTION_MARK, States.OSProcessingInstruction1);
		putDeltaToAllBut(States.OSProcessingInstruction1, TokenType.QUESTION_MARK, States.OSProcessingInstruction);
		deltas.get(States.OSProcessingInstruction).put(TokenType.QUOT, States.OSPIStringQuot);
		deltas.get(States.OSPIStringQuot).put(TokenType.QUOT, States.OSProcessingInstruction);
		putDeltaToAllBut(States.OSPIStringQuot, TokenType.QUOT, States.OSPIStringQuot);
		deltas.get(States.OSProcessingInstruction).put(TokenType.APOS, States.OSPIStringApos);
		deltas.get(States.OSPIStringApos).put(TokenType.APOS, States.OSProcessingInstruction);
		putDeltaToAllBut(States.OSPIStringApos, TokenType.APOS, States.OSPIStringApos);

		deltas.get(States.XmlOuter).put(TokenType.LESS_THAN, States.XmlOuterLT);

		/* Texte sind Kommentare */
		putDeltaToAllBut(States.XmlOuter, TokenType.LESS_THAN, States.XmlOuter);

		/* http://www.w3.org/TR/xml11/#IDAMMIS */
		deltas.get(States.XmlOuterLT).put(TokenType.SLASH, States.XOClose0);
		deltas.get(States.XOClose0).put(TokenType.GREATER_THAN, States.XmlOuter);
		deltas.get(States.XOClose0).put(TokenType.IDENTIFIER, States.XOClose);
		deltas.get(States.XOClose).put(TokenType.GREATER_THAN, States.XmlOuter);
		deltas.get(States.XOClose).put(TokenType.WHITESPACE, States.XOClose);

		/* http://www.w3.org/TR/xml11/#sec-comments */
		deltas.get(States.XmlOuterLT).put(TokenType.EXCLAMATION_MARK, States.XOComment0);
		deltas.get(States.XOComment0).put(TokenType.MINUS, States.XOComment1);
		deltas.get(States.XOComment1).put(TokenType.MINUS, States.XOComment);
		deltas.get(States.XOComment).put(TokenType.MINUS, States.XOComment2);
		putDeltaToAllBut(States.XOComment2, TokenType.MINUS, States.XOComment);
		deltas.get(States.XOComment2).put(TokenType.MINUS, States.XOComment3);
		deltas.get(States.XOComment3).put(TokenType.GREATER_THAN, States.XmlOuterLT);

		/* http://www.w3.org/TR/xml11/#sec-pi */
		deltas.get(States.XmlOuterLT).put(TokenType.QUESTION_MARK, States.XOProcessingInstruction0);
		deltas.get(States.XOProcessingInstruction0).put(TokenType.IDENTIFIER, States.XOProcessingInstruction);
		deltas.get(States.XOProcessingInstruction).put(TokenType.WHITESPACE, States.XOProcessingInstruction);
		deltas.get(States.XOProcessingInstruction).put(TokenType.QUESTION_MARK, States.XOProcessingInstruction1);
		deltas.get(States.XOProcessingInstruction1).put(TokenType.GREATER_THAN, States.XmlOuter);
		deltas.get(States.XOProcessingInstruction1).put(TokenType.QUESTION_MARK, States.XOProcessingInstruction1);
		putDeltaToAllBut(States.XOProcessingInstruction1, TokenType.QUESTION_MARK, States.XOProcessingInstruction);
		deltas.get(States.XOProcessingInstruction).put(TokenType.QUOT, States.XOPIStringQuot);
		deltas.get(States.XOPIStringQuot).put(TokenType.QUOT, States.XOProcessingInstruction);
		putDeltaToAllBut(States.XOPIStringQuot, TokenType.QUOT, States.XOPIStringQuot);
		deltas.get(States.XOProcessingInstruction).put(TokenType.APOS, States.XOPIStringApos);
		deltas.get(States.XOPIStringApos).put(TokenType.APOS, States.XOProcessingInstruction);
		putDeltaToAllBut(States.XOPIStringApos, TokenType.APOS, States.XOPIStringApos);

		deltas.get(States.OuterScopeLT).put(TokenType.IDENTIFIER, States.XmlTag);
		deltas.get(States.XmlOuterLT).put(TokenType.IDENTIFIER, States.XmlTag);
		deltas.get(States.XmlTag).put(TokenType.WHITESPACE, States.XmlTag);
		deltas.get(States.XmlTag).put(TokenType.GREATER_THAN, States.XmlOuter);
		deltas.get(States.XmlTag).put(TokenType.SLASH, States.XTNET);
		deltas.get(States.XTNET).put(TokenType.GREATER_THAN, States.XmlOuter); /* null end tag */
		deltas.get(States.XmlTag).put(TokenType.IDENTIFIER, States.XTAttr0);
		deltas.get(States.XTAttr0).put(TokenType.WHITESPACE, States.XmlTag);
		deltas.get(States.XTAttr0).put(TokenType.EQUALS, States.XTAttr1);
		deltas.get(States.XTAttr1).put(TokenType.REAL, States.XTAttr2);
		deltas.get(States.XTAttr1).put(TokenType.INTEGER, States.XTAttr2);
		deltas.get(States.XTAttr1).put(TokenType.IDENTIFIER, States.XTAttr2);
		deltas.get(States.XTAttr1).put(TokenType.QUOT, States.XTAttrQuot);
		deltas.get(States.XTAttr1).put(TokenType.APOS, States.XTAttrApos);
		deltas.get(States.XTAttr2).put(TokenType.REAL, States.XTAttr2);
		deltas.get(States.XTAttr2).put(TokenType.INTEGER, States.XTAttr2);
		deltas.get(States.XTAttr2).put(TokenType.IDENTIFIER, States.XTAttr2);
		deltas.get(States.XTAttr2).put(TokenType.QUOT, States.XTAttrQuot);
		deltas.get(States.XTAttr2).put(TokenType.APOS, States.XTAttrApos);
		deltas.get(States.XTAttr2).put(TokenType.WHITESPACE, States.XmlTag);
		deltas.get(States.XTAttrQuot).put(TokenType.QUOT, States.XTAttr2);
		putDeltaToAllBut(States.XTAttrQuot, TokenType.QUOT, States.XTAttrQuot);
		deltas.get(States.XTAttrApos).put(TokenType.APOS, States.XTAttr2);
		putDeltaToAllBut(States.XTAttrApos, TokenType.APOS, States.XTAttrApos);
	}
	
	protected States state;
	
	protected final TokenInputStream in;
	
	public XmlInputStream() {
		this(new TokenInputStream());
	}
	
	public XmlInputStream(TokenInputStream in) {
		this.in = in;
	}
	
	@Override
	public void close() throws IOException {
		in.close();
	}
	
	protected Deque<Token> readTokens = new LinkedList<Token>();
	protected Token readToken() throws IOException {
		final Token first = readTokens.pollFirst();
		if(first != null) {
			return first;
		}
		return in.readNextToken();
	}
	protected void unread(Token token) {
		readTokens.addFirst(token);
	}
	
	/*
	 * OuterScope -> OuterScope(PROCESSING_INSTRUCTION)
	 *            -> XmlTag(ELEMENT)
	 * 
	 * XmlTag     -> XmlTag(ATTR_INT)
	 *            -> XmlTag(ATTR_REAL)
	 *            -> XmlTag(ATTR_TEXT)
	 *            -> XmlTag(ATTR_NULL)
	 *            -> XmlOuter(CLOSING)
	 * 
	 * XmlOuter   -> XmlOuter(PROCESSING_INSTRUCTION)
	 *            -> XmlOuter(CLOSING)
	 *            -> XmlTag(ELEMENT)
	 */
	public XmlElement readNextElement() throws IOException {
		Token token = readToken();
		if(token.isEnd()) {
			return new XmlElement(XmlElementType.EOF, token.stream, token.start, token.end);
		} else if(token.isError()) {
			return new XmlElement(XmlElementType.ERROR, token.stream, token.start, token.end);
		}
		
		final Token first = token;
		for(;;) {
			// XXX: In Arbeit, René
			
			if(token.isError()) {
				return new XmlElement(XmlElementType.ERROR, token.stream, first.start, token.end);
			} else if(token.isError()) {
				return new XmlElement(XmlElementType.ERROR, token.stream, token.start, token.end);
			}
		}
	}
	
}
