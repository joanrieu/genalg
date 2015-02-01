package net.fififox.genalg;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GeneticSolver {

	public static interface Individual {

		boolean isSameAs(Individual o);

		double getFitness();

		Individual mutate();

		Individual cross(Individual o);

		long getGeneration();

		void die();

	}

	public class IndividualComparator implements Comparator<Individual> {
		@Override
		public int compare(Individual o1, Individual o2) {
			int c = Double.compare(o1.getFitness(), o2.getFitness());
			if (c != 0)
				return -c;
			c = Long.compare(o1.getGeneration(), o2.getGeneration());
			if (c != 0)
				return -c;
			return Integer.compare(hashCode(), o2.hashCode());
		}
	}

	public SortedSet<Individual> population;
	public List<Individual> newborns;

	public int selectedPopulation;
	public double mutationProbability;
	public double crossoverProbability;

	public GeneticSolver(int selectedPopulation, double mutationProbability,
			double crossoverProbability) {
		this.selectedPopulation = selectedPopulation;
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		IndividualComparator comparator = new IndividualComparator();
		population = new TreeSet<>(comparator);
		newborns = new LinkedList<>();
	}

	public Individual getBestFit(double fitnessGoal, int maxIterations) {
		while (population.first().getFitness() < fitnessGoal
				&& maxIterations-- > 0) {
			step();
			dumpBestFit(maxIterations);
		}
		return population.first();
	}

	public Individual getPlateauFit(int waitIterations, int maxIterations) {
		Individual best = null;
		int wait = 0;
		while (wait < waitIterations && maxIterations-- > 0) {
			step();
			if (best == null || !population.first().isSameAs(best)) {
				best = population.first();
				wait = 0;
			} else {
				++wait;
			}
			dumpBestFit(maxIterations);
		}
		return best;
	}

	public void step() {
		mutate();
		cross();
		select();
	}

	private void select() {
		newborns.forEach(i -> {
			population.add(i);
		});
		newborns.clear();
		while (population.size() > selectedPopulation)
			population.remove(population.last());
	}

	private void mutate() {
		for (Individual individual : population)
			if (Math.random() < mutationProbability)
				addNewborn(individual.mutate());
	}

	private void cross() {
		double probability = crossoverProbability * crossoverProbability;
		population.forEach(i1 -> {
			if (Math.random() < probability) {
				population.forEach(i2 -> {
					if (Math.random() < probability) {
						addNewborn(i1.cross(i2));
					}
				});
			}
		});
	}

	private synchronized void addNewborn(Individual individual) {
		newborns.add(individual);
	}

	private void dumpBestFit(int iterations) {
		System.err.println("[" + iterations + "] " + population.first());
	}

}
