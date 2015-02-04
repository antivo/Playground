package hr.fer.zemris.optjava.threesat;

public class SATFormulaStats {
	private static final double percentageConstantUp = 0.01;
	private static final double percentageConstantDown = 0.1;
	private static final double percentageUnitAmmount = 50;
	
	private SATFormula satFormula;
	private int numOfSatisfied;
	private boolean isSatisfied;
	private double percentageBonus;
	private double percentage[];
	
	public SATFormulaStats(SATFormula satFormula) {
		this.satFormula = satFormula;
		int numOfClauses = satFormula.getNumberOfClauses();
		percentage = new double[numOfClauses];
		for(int i = 0; i < percentage.length; ++i) {
			percentage[i] = 0;
		}
	}
	
	public void setAssigment(BitVector assignment, boolean updatePercentages) {
		numOfSatisfied = 0;
		isSatisfied = true;
		percentageBonus = 0;
		for(int i = 0; i < satFormula.getNumberOfClauses(); ++i) {
			boolean satisfies = satFormula.satisfiesClause(assignment, i);
			if(satisfies) {
				++numOfSatisfied;
				if(updatePercentages) {
					percentage[i] += (1 - percentage[i]) * percentageConstantUp;
				} else {
					percentageBonus += percentageUnitAmmount * (1 - percentage[i]); 
				}
			} else {
				isSatisfied = false;	
				if(updatePercentages) {
					percentage[i] += (0 - percentage[i]) * percentageConstantDown;
				} else {
					percentageBonus -= percentageUnitAmmount * (1 - percentage[i]);
				}
			}
		}
	}
	
	public int getNumberOfSatisfied() {
		return numOfSatisfied;
	}
	
	public boolean issSatisfied() {
		return isSatisfied;
	}
	
	public double getPercentageBonus() {
		return percentageBonus;
	}
	
	public double getPercentage(int index) {
		return percentage[index];
	}
}
