package test;

import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser;

public class StatementParserTest {
	public static void main(String[] args) {
		String[] rvalue_tests = new String[] { 
				/*"\"abc\" - abc - 123 - 1.23",
				"--a++ + !b <= 1.0",
				"a+b*2",
				"1+2-3*4/5%6",
				"1 & 2 | 3 ^ 4",
				"a && b || c",*/
				"a[123+456]",
				"a.b[1+2].c(1) + 2"
		};
		String[] lvalue_tests = new String[] { 
				"a.b[1+2].c(1)",
				"a.b().c[1]"
		};
		
		StatementParser parser = new StatementParser();
		/*for (String str : rvalue_tests) {
			System.out.println("== testing rvalue \"" + str + "\" ===");
			Expression expr = parser.parse(str, false);
			expr.printTree(0);
		}*/
		
		for (String str : lvalue_tests) {
			System.out.println("== testing lvalue \"" + str + "\" ===");
			Expression expr = parser.parse(str, true);
			expr.printTree(0);
		}
	}
}
