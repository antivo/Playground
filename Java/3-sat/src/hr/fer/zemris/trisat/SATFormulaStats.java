package hr.fer.zemris.trisat;

public class SATFormulaStats {
	private SATFormula formula;
	
	private double post[];
	private double percentageConstantUp;
	private double percentageConstantDown; 
	private double percentageBonus;
	
	private int numberOfSatisfied;
	private boolean isSatisfied;
	
	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
	}
	
	public SATFormulaStats(SATFormula formula, double percentageConstantUp, double percentageConstantDown) {
		this(formula);
		int size = formula.getNumberOfClauses();
		post = new double[size];
		this.percentageConstantUp = percentageConstantUp;
		this.percentageConstantDown = percentageConstantDown;
	}
	
	
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		int size = formula.getNumberOfClauses();
		numberOfSatisfied = 0;
		
		if(updatePercentages) {
			percentageBonus = 0;
		}
		
		for(int i = 0; i < size; ++i) {
			Clause clause = formula.getClause(i);
			boolean clauseIsSatisfied = clause.isSatisfied(assignment);
			if(clauseIsSatisfied) {
				++numberOfSatisfied;
			}
			
			if(updatePercentages) {
				if(clauseIsSatisfied) {
					post[i] += (1-post[i]) * percentageConstantUp;
					percentageBonus += (1-post[i]);
				} else {
					post[i] += (0-post[i]) * percentageConstantDown;
					percentageBonus -= (1-post[i]);
				}
			}
		}
		
		if(size == numberOfSatisfied) {
			isSatisfied = true;
		} else {
			isSatisfied = false;
		}
	}
	
	public int getNumberOfSatisfied() {
		return numberOfSatisfied;
	}
	
	public boolean isSatisfied() {
		return isSatisfied;
	}
	
	public double getPercentageBonus() {
		return percentageBonus;
	}
}
