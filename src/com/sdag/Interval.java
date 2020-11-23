package com.sdag;

public class Interval {

	private int min, max;
	
	public Interval(int[] intIntervals) {
		min = intIntervals[0];
		max = intIntervals[1];
	}

	public Interval(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	public String toString() {
		return "[" + getMin() + "," + getMax() + "]";
	}

	public boolean matches(Interval testInterval) {
		return testInterval.getMin() == getMin() && testInterval.getMax() == getMax();
	}
}
