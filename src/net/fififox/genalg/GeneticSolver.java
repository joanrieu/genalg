package net.fififox.genalg;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GeneticSolver {

	public static interface Individual {

		Individual mutate();

		Individual cross(Individual o);

	}

	public SortedSet<Individual> population;
	public List<Individual> newborns = new LinkedList<>();

	public int selectedPopulation;
	public double mutationProbability;
	public double crossoverProbability;

	public GeneticSolver(int selectedPopulation, double mutationProbability,
			double crossoverProbability) {
		population = new TreeSet<>();
		this.selectedPopulation = selectedPopulation;
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
	}

	public Individual getPlateauFit(int waitIterations, int maxIterations) {
		Individual best = null;
		int wait = 0;
		for (int i = 0; i < maxIterations && wait < waitIterations; ++i) {
			step();
			System.err.print("\r" + population.first());
			if (!population.first().equals(best)) {
				best = population.first();
				wait = 0;
			} else {
				++wait;
			}
		}
		System.err.println();
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
		final double probability = crossoverProbability * crossoverProbability;
		for (Individual i1 : population)
			if (Math.random() < probability)
				for (Individual i2 : population)
					if (Math.random() < probability)
						addNewborn(i1.cross(i2));
	}

	private synchronized void addNewborn(Individual individual) {
		newborns.add(individual);
	}

}
