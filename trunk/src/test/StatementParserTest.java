package test;

import de.fu_berlin.compilerbau.parser.expressions.Expression;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser;
import de.fu_berlin.compilerbau.statementParser.impl.StatementParser.ExpressionType;

class StatementParserTest {
	public static void main(String[] args) {		
		String[] rvalue_tests = new String[] { 
				/*"\"abc\" - abc - 123 - 1.23",
				"--a++ + !b <= 1.0",
				"a+b*2",
				"1+2-3*4/5%6",
				"1 & 2 | 3 ^ 4",
				"a && b || c",*/
				"a[123+456]",
				"a.b[1+2].c(1) + new Klasse(1,2,3)",
				"{1,2,3,4} + {'a', 'b', 'c', 'd'}"
		};
		String[] lvalue_tests = new String[] { 
				"a.b[1].c(1)",
				"a.b(1).c[1]"
		};
		String[] functioncall_tests = new String[] { 
				"a[123+456].xyz(13)",
				"a.b[1+2].c(1)",
				"a().c"
		};
				
		StatementParser parser = new StatementParser();
		for (String str : rvalue_tests) {
			System.out.println("== testing rvalue \"" + str + "\" ===");
			Expression expr = parser.parse(str, ExpressionType.RVALUE);
			expr.printTree(0);
		}
		
		for (String str : lvalue_tests) {
			System.out.println("== testing lvalue \"" + str + "\" ===");
			Expression expr = parser.parse(str, ExpressionType.LVALUE);
			expr.printTree(0);
		}
		
		for (String str : functioncall_tests) {
			System.out.println("== testing function call \"" + str + "\" ===");
			Expression expr = parser.parse(str, ExpressionType.FUNCTIONCALL);
			expr.printTree(0);
		}
	}
}
