package test.junit;


import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.fu_berlin.compilerbau.dom.DomCreator;
import de.fu_berlin.compilerbau.dom.DomNode;
import de.fu_berlin.compilerbau.parser.AbstractSyntaxTree;
import de.fu_berlin.compilerbau.parser.Module;

public class TestAbstractSyntaxTreeGGT extends TestCase {
	
	AbstractSyntaxTree tree;
		
	@Before
	public void setUp() throws Exception {
			FileReader in = new FileReader(new File("testfiles/ggt.xml"));
			DomCreator.init(in);
			
			//XML parsen
			DomNode node = DomCreator.createDOM();
			tree = new AbstractSyntaxTree(node);
		}
	
	
	/**
	 * Test method for 
	 */
	@Test
	public void testAbstractSyntaxTree() {
		assertNotNull(tree);
	}
	
	public void testModule(){
		Module module = 		tree.getRoot();
		assertTrue(module instanceof Module);
		assertEquals("mathe", tree.getRoot().getName().toString());
	}
}
