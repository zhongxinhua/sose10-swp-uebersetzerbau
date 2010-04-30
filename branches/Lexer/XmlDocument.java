public class XmlDocument {
	
	protected static final class ToRemove {
		public final CharSequence tag;
		public final ToRemove prev;
		
		public ToRemove(CharSequence tag, ToRemove prev) {
			this.tag = tag;
			this.prev = prev;
		}
	}
	
	protected static ToRemove nextToRemove = null;
	protected void addToRemove(CharSequence tag) {
		nextToRemove = new ToRemove(tag, nextToRemove);
	}
	
	protected static enum RemoveResult {
		/** the topmost element was popped */
		REMOVED_TOP,
		/** more than one element was popped */
		REMOVED_SOME,
		/** the root element was popped */
		REMOVED_ROOT,
		/** tag was not found */
		REMOVED_NONE
	}
	/**
	 * @throws IllegalStateException There was no root (anymore)
	 */
	protected RemoveResult remove(CharSequence tag) throws IllegalStateException {
		if(nextToRemove == null) {
			throw new IllegalStateException("There was no root (anymore)");
		}
		
		if(nextToRemove.tag.equals(tag)) {
			nextToRemove = nextToRemove.prev;
			return RemoveResult.REMOVED_TOP;
		}
		
		for(ToRemove i = nextToRemove.prev; i != null; i = i.prev) {
			if(i.tag.equals(tag)) {
				nextToRemove = i.prev;
				if(nextToRemove != null) {
					return RemoveResult.REMOVED_SOME;
				} else {
					return RemoveResult.REMOVED_ROOT;
				}
			}
		}
		
		return RemoveResult.REMOVED_NONE;
	}
	
}
