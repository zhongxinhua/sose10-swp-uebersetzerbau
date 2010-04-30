public class ProcessingInstruction extends XmlElement {
	
	public final CharSequence body;
	
	public ProcessingInstruction(TokenInputStream stream, CharSequence body, int start, int end) {
		super(XmlElementType.PROCESSING_INSTRUCTION, stream, start, end);
		this.body = body;
	}
	
	@Override
	public <Dest extends Appendable> Dest appendTo(Dest dest) throws java.io.IOException {
		super.appendTo(dest);
		dest.append("<");
		dest.append(body);
		dest.append(">");
		return dest;
	}
	
} 
