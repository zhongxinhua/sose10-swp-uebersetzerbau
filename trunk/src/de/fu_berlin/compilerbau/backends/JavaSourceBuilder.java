package de.fu_berlin.compilerbau.backends;

import de.fu_berlin.compilerbau.directoryWriter.DirectoryWriter;
import de.fu_berlin.compilerbau.parser.ArgumentStatement;
import de.fu_berlin.compilerbau.parser.AssignStatement;
import de.fu_berlin.compilerbau.parser.BreakStatement;
import de.fu_berlin.compilerbau.parser.CallStatement;
import de.fu_berlin.compilerbau.parser.Case;
import de.fu_berlin.compilerbau.parser.ChooseStatement;
import de.fu_berlin.compilerbau.parser.Class;
import de.fu_berlin.compilerbau.parser.ContinueStatement;
import de.fu_berlin.compilerbau.parser.DeclarationStatement;
import de.fu_berlin.compilerbau.parser.DoStatement;
import de.fu_berlin.compilerbau.parser.Function;
import de.fu_berlin.compilerbau.parser.ImplementStatement;
import de.fu_berlin.compilerbau.parser.ImportStatement;
import de.fu_berlin.compilerbau.parser.Interface;
import de.fu_berlin.compilerbau.parser.Module;
import de.fu_berlin.compilerbau.parser.ReturnStatement;
import de.fu_berlin.compilerbau.parser.ScopeStatement;
import de.fu_berlin.compilerbau.parser.StaticStatement;

public class JavaSourceBuilder implements Builder {
	
	final DirectoryWriter directoryWriter;
	
	public JavaSourceBuilder(DirectoryWriter directoryWriter) {
		this.directoryWriter = directoryWriter;
	}

	@Override
	public void argumentStatementCallback(ArgumentStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignStatementCallback(AssignStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakStatementCallback(BreakStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callStatementCallback(CallStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void caseCallback(Case obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chooseStatementCallback(ChooseStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void classCallback(Class obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void continueStatementCallback(ContinueStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declarationStatementCallback(DeclarationStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doStatementCallback(DoStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void functionCallback(Function obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void implementStatementCallback(ImplementStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importStatementCallback(ImportStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interfaceCallback(Interface obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moduleCallback(Module obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnStatementCallback(ReturnStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scopeStatementCallback(ScopeStatement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void staticStatementCallback(StaticStatement obj) {
		// TODO Auto-generated method stub
		
	}
	
}
