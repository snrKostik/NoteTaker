package com.example.notetaker_java.SpacedRepetition;


import static java.lang.Math.log;

import java.text.BreakIterator;

public class SpacedRepetition {
	private boolean isRepeated; // checkbox
	private boolean needToRepeat; // checkbox

	private double formula(double t, double k, double c){
		double b;
		b = (100 * k) / ((log(t)^c) + k);
		return b;
	}
}
