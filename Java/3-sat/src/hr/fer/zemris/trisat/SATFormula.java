package hr.fer.zemris.trisat;

public class SATFormula {
	private int numberOfVariables;
	private Clause clauses[];
	
	public SATFormula(int numberOfVariables, Clause[] clauses) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses;
	}
	
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	public int getNumberOfClauses() {
		return clauses.length;
	}
	
	public Clause getClause(int index) {
		return clauses[index];
	}
	
	public boolean isSatisfied(BitVector assigment) {
		for(Clause clause : clauses) {
			if (!clause.isSatisfied(assigment)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder ss = new StringBuilder();
		for(Clause clause : clauses) {
			ss.append('(').append(clause.toString()).append(')');
		}
		return ss.toString();
	}
}
