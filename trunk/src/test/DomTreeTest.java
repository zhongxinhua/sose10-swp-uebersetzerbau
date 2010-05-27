package test;

/**
 * @author stefan
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.fu_berlin.compilerbau.dom.DomAttribute;
import de.fu_berlin.compilerbau.dom.DomCreator;
import de.fu_berlin.compilerbau.dom.DomNode;

public class DomTreeTest {
	static StringBuffer sb;
	
	public static void main(String args[]) {
		DomTreeTest test = new DomTreeTest();
		System.out.println("Test done: " + test);
	}
	
	public DomTreeTest() {
		sb = new StringBuffer();
		try {
			DomCreator.init(new FileReader(new File("testfiles\\ggt.xml")));
		} catch (Exception e) {}
		
		DomNode node = DomCreator.createDOM();
		printNode(node);
		
		try {
			FileWriter fw = new FileWriter(new File("testfiles\\dom_test.xml"));
			fw.append(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printNode(DomNode domtree) {
		sb.append("<");
		sb.append(domtree.getName().toString() + " ");
		for(DomAttribute attr: domtree.getAttributes()) {
			sb.append(attr.getName().toString() + "=\"");
			sb.append(attr.getValue().toString() + "\" ");
		}
		if(domtree.getChilds().isEmpty()) {
			sb.append("/>\n");
		}
		else {
			sb.append(">\n");
			for(DomNode child : domtree.getChilds()) {
				printNode(child);
			}
			sb.append("</" + domtree.getName().toString() + ">\n");
		}
	}
	
}
