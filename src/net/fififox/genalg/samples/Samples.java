package net.fififox.genalg.samples;

import net.fififox.genalg.GeneticSolver;

public class Samples {

	public static void main(String[] args) {
		GeneticSolver solver = new GeneticSolver(200, 1, .2);
		String solution = "An exponential moving average is a type of infinite impulse response filter that applies weighting factors which decrease exponentially";
		for (int i = 0; i < solver.selectedPopulation; ++i)
			solver.population.add(new StringFinder(solution));
		System.err.println(((StringFinder) solver.getPlateauFit(100, 10000))
				.isSolution() ? "CORRECT" : "FAILURE");
	}
}
