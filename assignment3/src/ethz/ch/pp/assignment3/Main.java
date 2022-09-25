// Author : Quentin Gigon
// Task A: the reason why the the value is not what we expect is because the blocks are not synchronized which causes bad interleavings
// Task C: the only discernible pattern seems to be that the thread 0 runs before all others 
// Task D: no idea how to proceed when two threads need to be in sync but have the same run method???
// Task F: Atomic Counter seems to be slower... perhaps because the value.getAndIncrement method of the AtomicInteger class is slower than value++?
// Task G: I'm not sure of what Counter in what task we are talking about... for which one are we supposed to measure the execution progress? Or did I misunderstand the ex? 

package ethz.ch.pp.assignment3;

import java.util.ArrayList;
import java.util.List;

import ethz.ch.pp.assignment3.counters.AtomicCounter;
import ethz.ch.pp.assignment3.counters.Counter;
import ethz.ch.pp.assignment3.counters.SequentialCounter;
import ethz.ch.pp.assignment3.counters.SynchronizedCounter;
import ethz.ch.pp.assignment3.threads.ThreadCounterFactory;
import ethz.ch.pp.assignment3.threads.ThreadCounterFactory.ThreadCounterType;

public class Main {

	public static void count(final Counter counter, int numThreads, ThreadCounterType type, int numInterations) {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < numThreads; i++) {
			threads.add(new Thread(ThreadCounterFactory.make(type, counter, i, numThreads, numInterations)));
		}

		for (int i = 0; i < numThreads; i++) {
			threads.get(i).start();
		}

		for (int i = 0; i < numThreads; i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		taskASequential();
		taskAParallel();
		long startTimeB = System.nanoTime();
		taskB();
		long endTimeB = System.nanoTime();
		long elapsedNsB = endTimeB - startTimeB;
		double elapsedMsB = elapsedNsB / 1.0e6;
		taskD();
		long startTimeE = System.nanoTime();
		taskE();
		long endTimeE = System.nanoTime();
		long elapsedNsE = endTimeE - startTimeE;
		double elapsedMsE = elapsedNsE / 1.0e6;
		System.out.println("Synchronized Counter takes" + elapsedMsB + "ms");
		System.out.println("Atomic Counter takes " + elapsedMsE + "ms");

	}

	public static void taskASequential() {
		Counter counter = new SequentialCounter();
		count(counter, 1, ThreadCounterType.NATIVE, 100000);
		System.out.println("Counter: " + counter.value());
	}

	public static void taskAParallel() {
		Counter counter = new SequentialCounter();
		count(counter, 4, ThreadCounterType.NATIVE, 100000);
		System.out.println("Counter: " + counter.value());
	}

	public static void taskB() {
		Counter counter = new SynchronizedCounter();
		count(counter, 4, ThreadCounterType.NATIVE, 100000);
		System.out.println("Counter: " + counter.value());
	}

	public static void taskD() {
		Counter counter = new SynchronizedCounter();
		count(counter, 2, ThreadCounterType.FAIR, 100000);
		System.out.println("Counter: " + counter.value());
	}

	public static void taskE() {
		Counter counter = new AtomicCounter();
		count(counter, 4, ThreadCounterType.NATIVE, 100000);
		System.out.println("Counter: " + counter.value());
	}

}
