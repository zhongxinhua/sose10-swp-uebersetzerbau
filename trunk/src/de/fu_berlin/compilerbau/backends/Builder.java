package de.fu_berlin.compilerbau.backends;

import de.fu_berlin.compilerbau.parser.*;
import de.fu_berlin.compilerbau.parser.Class;

public interface Builder {
	
	void argumentStatementCallback(ArgumentStatement obj);
	void assignStatementCallback(AssignStatement obj);
	void breakStatementCallback(BreakStatement obj);
	void callStatementCallback(CallStatement obj);
	void caseCallback(Case obj);
	void chooseStatementCallback(ChooseStatement obj);
	void classCallback(Class obj);
	void continueStatementCallback(ContinueStatement obj);
	void declarationStatementCallback(DeclarationStatement obj);
	void doStatementCallback(DoStatement obj);
	void functionCallback(Function obj);
	void implementStatementCallback(ImplementStatement obj);
	void importStatementCallback(ImportStatement obj);
	void interfaceCallback(Interface obj);
	void moduleCallback(Module obj);
	void returnStatementCallback(ReturnStatement obj);
	void scopeStatementCallback(ScopeStatement obj);
	void staticStatementCallback(StaticStatement obj);
	
}
