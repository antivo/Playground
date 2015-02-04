package hr.fer.zemirs.fenc.set.builder;

import hr.fer.zemirs.fenc.container.EntityContainer;
import hr.fer.zemirs.fenc.container.UndefinedEntity;
import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.operator.Operator;

public class FuzzySetBuilderFromExpression {
	private static String[] operatorPriorities = new String[] {"->", "*", "+", "!"};
	
	private static String removeParenthesis(String expression) {
		int last = expression.length() - 1; 
		if(expression.charAt(0) == '(') {
			if(expression.charAt(last) == ')') {
				expression = expression.substring(1, last);
			}
		}
		
		return expression;
	}
	
	public static FuzzySet parseExpression(String expression, EntityContainer<FuzzySet> fuzzySetContainer, EntityContainer<Operator> operatorContainer) throws DeclarationException, UndefinedEntity {
		return parseExpression(expression, fuzzySetContainer, operatorContainer, 0);
	}
	
	private static FuzzySet parseExpression(String expression, EntityContainer<FuzzySet> fuzzySetContainer, EntityContainer<Operator> operatorContainer, int operatorIndex) throws DeclarationException, UndefinedEntity {
		if(0 == expression.length()) {
			throw new DeclarationException("Cannot create from empty expression");
		}
		
		expression = removeParenthesis(expression);
		String operatorSymbol = operatorPriorities[operatorIndex];
		
		if(fuzzySetContainer.contains(expression)) {
			return fuzzySetContainer.get(expression);
		}
		
		Operator operator = operatorContainer.get(operatorSymbol);
		int nextOperatorIndex = (operatorIndex + 1) % operatorPriorities.length;
		
		
		// TODO recursive  
		
		return null;
	}
}
