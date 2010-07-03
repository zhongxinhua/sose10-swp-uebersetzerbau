package test;

import java.io.*;

import de.fu_berlin.compilerbau.statementLexer.StatementNode;
import de.fu_berlin.compilerbau.statementLexer.impl.StatementLexer;
import de.fu_berlin.compilerbau.util.PositionBean;
import de.fu_berlin.compilerbau.util.PositionString;

class StatementLexerTest {
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int lineNum = 0;
		String line;
		while((line = reader.readLine()) != null) {
			PositionBean position = new PositionBean(1, ++lineNum, 1);
			PositionString string = new PositionString(line, position);
			Iterable<StatementNode> tokens = StatementLexer.tokenize(string);
			for(StatementNode node : tokens) {
				System.out.println(node);
			}
		}
	}
	
}
