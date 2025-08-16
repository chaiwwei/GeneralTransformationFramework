package com;

public class Timer {

	long current;

	public void start() {
		current = System.nanoTime();
	}

	public long elapsed() {
		return System.nanoTime() - current;
	}
}
