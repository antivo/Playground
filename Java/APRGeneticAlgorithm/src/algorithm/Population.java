package algorithm;

import java.util.Random;

import function.IFunction;

public class Population {
	private final int size;
	final private double pm;
	private final Random rand;
	private final IFunction fun;
	private Jedinka[] population;
	private double best;
	private int bestIdx;
	
	public Population(IFunction f, int size, double prom, Random r) {
		pm = prom;
		fun = f;
		rand = r;
		this.size = size;
		
		this.population = new Jedinka[size];
		int dim = f.getDimm();
		boolean bestNotChoosen = true;
		for(int i = 0; i < size; ++i) {
			population[i] = new Jedinka(r, dim);
			
			if(bestNotChoosen) {
				bestIdx = i;
				best = population[i].getFitness(fun);
			} else {
				double t = population[i].getFitness(fun);
				if(best > t) {
					best = t;
					bestIdx = i;
				}
			}
		}
	}
	
	public Jedinka iterate() {
		int[] originals = new int[3];
		Jedinka[] three = new Jedinka[3];
		for(int i = 0; i < 3; ++i) {
			int index = rand.nextInt(size);
			three[i] = population[index];
			originals[i] = index;
		}
		
		//worst
		int worst = 0;
		double max = three[0].getFitness(fun);
		if(max < three[1].getFitness(fun)) {
			worst = 1;
			max = three[1].getFitness(fun);
		}
		if(max < three[2].getFitness(fun)) {
			worst = 2;
			max = three[2].getFitness(fun);
		}
		//
		Jedinka[] toCross = new Jedinka[2];
		int idxToCross = 0;
		for(int i = 0; i < 3; ++i) {
			if(i != worst) {
				toCross[idxToCross] = three[i];
				idxToCross++;
			}
		}
		//
		Jedinka child = cross(toCross[0], toCross[1]);
		child.mutate(pm);
		//
		int toReplace = originals[worst];
		population[toReplace] = child;
		
		if(best > child.getFitness(fun)) {
			best = child.getFitness(fun);
			bestIdx = toReplace;
		}
		
		return population[bestIdx];
	}
	
	private Jedinka cross(Jedinka j1, Jedinka j2) {
		int dim = j1.koordinate.length;
		
		Jedinka ret = new Jedinka(rand, dim);
		for(int i = 0; i < dim; ++i) {
			double firstParent = j1.koordinate[i];
			double secondParent = j2.koordinate[i];
			ret.koordinate[i] = (firstParent + secondParent) / 2;
		}
		return ret;
	}
	
	public Jedinka getBest() {
		return population[bestIdx];
	}
	
}
