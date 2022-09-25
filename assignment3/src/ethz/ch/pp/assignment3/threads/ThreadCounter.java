package ethz.ch.pp.assignment3.threads;

import ethz.ch.pp.assignment3.counters.Counter;

public abstract class ThreadCounter implements Runnable {
	Counter counter;
	protected final int id;
	protected final int numThreads;
	protected final int numIterations;
	
	int value;

	public synchronized void increment() {
		value++;
		notify();
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public synchronized int value() {
		return value;
	}

	public ThreadCounter(Counter counter, int id, int numThreads, int numIterations) {
		this.counter = counter;
		this.id = id;
		this.numThreads = numThreads;
		this.numIterations = numIterations;
	}

}
