/**
 * 
 */
package test.junit;

import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

import org.junit.Test;

import de.fu_berlin.compilerbau.dom.DomCreator;
import de.fu_berlin.compilerbau.dom.DomNode;


/**
 * @author Ansgar
 *
 */
public class TestNodeGGT extends TestCase{
	
	DomNode node;
	
	@Override
	protected void setUp() throws Exception {
		FileReader in = new FileReader(new File("testfiles/ggt.xml"));
		DomCreator.init(in);
		
		//XML parsen
		node = DomCreator.createDOM();
	}
	
	/**
	 * Test method for 
	 */
	@Test
	public void testNode() {
		assertEquals(true, node.isNode());
		assertEquals(false, node.isLeaf());
		
	}
	
}
