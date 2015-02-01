package net.fififox.genalg;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GeneticSolver {

	public static interface Individual {

		boolean isSameAs(Individual o);

		Individual mutate();

		Individual cross(Individual o);

		void die();

	}

	public SortedSet<Individual> population;
	public List<Individual> newborns = new LinkedList<>();
	public int iteration = 0;

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
		while (wait < waitIterations && maxIterations-- > 0) {
			step();
			if (!population.first().isSameAs(best)) {
				best = population.first();
				wait = 0;
			} else {
				++wait;
			}
		}
		return best;
	}

	public void step() {
		dump();
		++iteration;
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
		population.forEach(i1 -> {
			if (Math.random() < probability) {
				population.forEach(i2 -> {
					if (Math.random() < probability)
						addNewborn(i1.cross(i2));
				});
			}
		});
	}

	private synchronized void addNewborn(Individual individual) {
		newborns.add(individual);
	}

	private void dump() {
		System.err.println("[" + iteration + "] " + population.first());
	}

}
