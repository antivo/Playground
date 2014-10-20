package hr.fer.zemris.optjava.threesat;

public class SATFormula {
	int numberOfVariables;
	Clause[] clauses;
	
	public boolean satisfiesClause(BitVector assigment, int clauseIdx) {
		return this.clauses[clauseIdx].isSatisfied(assigment);
	}
	
	public SATFormula(int numberOfVariables, Clause[] clauses) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses.clone();
	}
	
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	public int getNumberOfClauses() {
		return clauses.length;
	}
	
	public int getNumberOfSatisfiedClauses(BitVector assigment) {
		int count = 0;
		for(Clause clause : clauses) {
			if(clause.isSatisfied(assigment)) {
				++count;
			}
		}
		return count;
	}
	
	public boolean isSatisfied(BitVector assigment) {
		return getNumberOfClauses() == getNumberOfSatisfiedClauses(assigment);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Clause clause : clauses) {
			sb.append('(');
			sb.append(clause.toString());
			sb.append(')');
		}
		return sb.toString();
	}
}
