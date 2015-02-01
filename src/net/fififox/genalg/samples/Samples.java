package net.fififox.genalg.samples;

import net.fififox.genalg.GeneticSolver;
import net.fififox.genalg.GeneticSolver.Individual;

public class Samples {

	public static void main(String[] args) {
		run(HelloWorld.class);
		// run(TravellingSalesman.class);
	}

	public static void run(Class<? extends GeneticSolver.Individual> c) {
		GeneticSolver solver = new GeneticSolver(1000, .2, .2);
		for (int i = 0; i < solver.selectedPopulation; ++i) {
			try {
				solver.population.add(c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Individual bestFit = solver.getPlateauFit(10, 1000);
		System.out.println(bestFit);
	}

}
