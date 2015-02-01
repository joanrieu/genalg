package net.fififox.genalg.samples;

import net.fififox.genalg.GeneticSolver;
import net.fififox.genalg.GeneticSolver.Individual;

public class HelloWorld implements GeneticSolver.Individual {

	private static final String solution = "An exponential moving average is a type of infinite impulse response filter that applies weighting factors which decrease exponentially";
	private String current;
	private long generation;

	public HelloWorld() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < solution.length(); ++i)
			builder.append(randomChar());
		current = builder.toString();
		generation = 1;
	}

	public HelloWorld(StringBuilder builder, long generation) {
		current = builder.toString();
		this.generation = generation;
	}

	@Override
	public boolean isSameAs(Individual o) {
		return current.equals(((HelloWorld) o).current);
	}

	@Override
	public double getFitness() {
		int points = 0;
		for (int i = 0; i < current.length(); ++i) {
			char c = current.charAt(i);
			if (c == solution.charAt(i))
				++points;
			if (solution.indexOf(c) != -1)
				++points;
		}
		return 1 - 1. / (1 + points);
	}

	@Override
	public Individual mutate() {
		StringBuilder builder = new StringBuilder(current);
		builder.setCharAt((int) (Math.random() * current.length()),
				randomChar());
		return new HelloWorld(builder, generation + 1);
	}

	@Override
	public Individual cross(Individual o) {
		StringBuilder builder = new StringBuilder(current);
		for (int i = 0; i < current.length(); ++i)
			if (Math.random() < .5)
				builder.setCharAt(i, ((HelloWorld) o).current.charAt(i));
		return new HelloWorld(builder, generation + 1);
	}

	@Override
	public long getGeneration() {
		return generation;
	}

	@Override
	public void die() {
	}

	@Override
	public String toString() {
		return current + "<" + getFitness() + "|" + getGeneration() + ">";
	}

	private char randomChar() {
		int r = (int) (Math.floor(Math.random() * 27));
		if (r == 26)
			return ' ';
		int c = 'a' + r;
		if (Math.random() < .5)
			c += 'A' - 'a';
		return (char) c;
	}

}
