package de.fu_berlin.compilerbau.statementParser.impl;

import java.util.HashMap;

import de.fu_berlin.compilerbau.statementLexer.TokenType;

public class PrecedenceTable {
	private static final PrecedenceTable instance = new PrecedenceTable();

	public static PrecedenceTable getInstance() {
		return instance;
	}

	public PrecedenceTable() {

	}

	public boolean isOperator(TokenType operator) {
		switch(operator) {
			case DOT:     
			case INCR:    
			case DECR:    
			case NOT:     
			case NEW:     
			case TIMES:   
			case DIVIDES: 
			case MOD:     
			case PLUS:    
			case MINUS:   
			case LE:      
			case GE:      
			case LT:      
			case GT:      
			case NE:      
			case EQ:      
			case BIT_AND: 
			case BIT_XOR: 
			case BIT_OR:  
			case AND:     
			case OR: return true;
		}
		return false;
	}

	public boolean isLeftAssociative(TokenType operator) {
		switch(operator) {
			case DOT:     return false;
			case INCR:    
			case DECR:    return false;
			case NOT:     
			case NEW:     return false;
			case TIMES:   
			case DIVIDES: 
			case MOD:     return true;
			case PLUS:    
			case MINUS:   return true;
			case LE:      
			case GE:      
			case LT:      
			case GT:      return true;
			case NE:      
			case EQ:      return true;
			case BIT_AND:
			case BIT_XOR:
			case BIT_OR: 
			case AND:    
			case OR:      return true;
			default:
				return true;
		}
	}

	public int getPriority(TokenType operator) {
		switch(operator) {
			case DOT:     return 14;
			case INCR:    
			case DECR:    return 13;
			case NOT:     
			case NEW:     return 12;
			case TIMES:   
			case DIVIDES: 
			case MOD:     return 10;
			case PLUS:    
			case MINUS:   return 9;
			case LE:      
			case GE:      
			case LT:      
			case GT:      return 7;
			case NE:      
			case EQ:      return 6;
			case BIT_AND: return 5;
			case BIT_XOR: return 4;
			case BIT_OR:  return 3;
			case AND:     return 2;
			case OR:      return 1;
		}
		return 0;
	}

	public int compareOperators(TokenType o1, TokenType o2) {
		int prio1 = getPriority(o1);
		int prio2 = getPriority(o2);
		if (prio1 == prio2)
			return 0;
		else if (prio1 > prio2)
			return 1;
		else
			return -1;
	}

}
