package test.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.junit");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestNodeGGT.class);
		//$JUnit-END$
		return suite;
	}

}
