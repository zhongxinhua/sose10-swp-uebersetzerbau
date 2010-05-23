package de.fu_berlin.compilerbau.parser;

import java.util.List;

/**
 * <b>*********** Not implemented yet! ***********</b>
 * <p/>
 * {@link ScopeStatement} is a subclass of {@link Statement} representing a
 * &ltscope/&gt statement forming a node in the parse tree.<br>
 * 
 * <p>
 * <b>Specification</b><br>
 * The &ltscope/&gt statement attributes is forbidden to exist.
 * 
 * The &ltscope/&gt statement body are {@link Statements}.
 * 
 * @author Sam
 * 
 */
public class ScopeStatement extends Statement {
	SymbolTable symbolTable;
	List<Statement> body;
}