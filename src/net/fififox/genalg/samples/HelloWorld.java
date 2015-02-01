package net.fififox.genalg.samples;

import net.fififox.genalg.GeneticSolver;
import net.fififox.genalg.GeneticSolver.Individual;

public class HelloWorld implements GeneticSolver.Individual,
		Comparable<HelloWorld> {

	private static final String solution = "An exponential moving average is a type of infinite impulse response filter that applies weighting factors which decrease exponentially";
	private String current;

	public HelloWorld() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < solution.length(); ++i)
			builder.append(randomChar());
		current = builder.toString();
	}

	public HelloWorld(StringBuilder builder) {
		current = builder.toString();
	}

	@Override
	public boolean isSameAs(Individual o) {
		return o instanceof Individual
				&& current.equals(((HelloWorld) o).current);
	}

	@Override
	public int compareTo(HelloWorld o) {
		final int a = getPoints(), b = o.getPoints();
		if (a != b)
			return b - a;
		return current.compareTo(o.current);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Individual && compareTo((HelloWorld) obj) == 0;
	}

	public int getPoints() {
		int points = 0;
		for (int i = 0; i < current.length(); ++i) {
			char c = current.charAt(i);
			if (c == solution.charAt(i))
				++points;
			if (solution.indexOf(c) != -1)
				++points;
		}
		return points;
	}

	@Override
	public Individual mutate() {
		StringBuilder builder = new StringBuilder(current);
		builder.setCharAt((int) (Math.random() * current.length()),
				randomChar());
		return new HelloWorld(builder);
	}

	@Override
	public Individual cross(Individual o) {
		StringBuilder builder = new StringBuilder(current);
		for (int i = 0; i < current.length(); ++i)
			if (Math.random() < .5)
				builder.setCharAt(i, ((HelloWorld) o).current.charAt(i));
		return new HelloWorld(builder);
	}

	@Override
	public void die() {
	}

	@Override
	public String toString() {
		return current;
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
