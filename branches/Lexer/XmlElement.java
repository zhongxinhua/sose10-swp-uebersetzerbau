public class XmlElement extends BasicToken {
	
	public final XmlElementType type;
	
	public XmlElement(XmlElementType type, TokenInputStream stream, int start, int end) {
		super(stream, start, end);
		this.type = type;
	}
	
	@Override
	public <Dest extends Appendable> Dest appendTo(Dest dest) throws java.io.IOException {
		dest.append(type.toString());
		super.appendTo(dest);
		return dest;
	}
	
}
