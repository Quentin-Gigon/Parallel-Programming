package ethz.ch.pp.assignment2;

import java.util.Arrays;
import java.util.Random;

class PrimeFactorsThread extends Thread {
	private int[] values;
	private int[] result;

	public PrimeFactorsThread(int[] values) {
		this.values = values;
	}

	public int[] getresult() {
		return result;
	}

	public void run() {
		this.result = Main.computePrimeFactors(values);
	}
}

class ThreadThread implements Runnable {
	String name;

	public ThreadThread(String x) {
		name = x;
	}

	public void run() {

		System.out.println("Hello world from " + name);
		System.out.println();
	}

}

class EmptyThread implements Runnable {
	public void run() {

	}
}

public class Main {

	public static void main(String[] args) {

		// TODO: adjust appropriately for the required experiments
		taskA();

		int[] input1 = generateRandomInput(1000);
		int[] input2 = generateRandomInput(10000);
		int[] input3 = generateRandomInput(100000);
		int[] input4 = generateRandomInput(1000000);

		// Sequential version
		taskB(input1);
		taskB(input2);
		taskB(input3);
		taskB(input4);

		// Parallel version
		taskE(input1, 4);
		taskE(input2, 4);
		taskE(input3, 4);
		taskE(input4, 4);

		long threadOverhead = taskC();
		System.out.format("Thread overhead on current system is: %d nano-seconds\n", threadOverhead);
	}

	private final static Random rnd = new Random(42);

	public static int[] generateRandomInput() {
		return generateRandomInput(rnd.nextInt(10000) + 1);
	}

	public static int[] generateRandomInput(int length) {
		int[] values = new int[length];
		for (int i = 0; i < values.length; i++) {
			values[i] = rnd.nextInt(99999) + 1;
		}
		return values;
	}

	public static int[] computePrimeFactors(int[] values) {
		int[] factors = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			factors[i] = numPrimeFactors(values[i]);
		}
		return factors;
	}

	public static int numPrimeFactors(int number) {
		int primeFactors = 0;
		int n = number;
		for (int i = 2; i <= n; i++) {
			while (n % i == 0) {
				primeFactors++;
				n /= i;
			}
		}
		return primeFactors;
	}

	public static class ArraySplit {
		public final int startIndex;
		public final int length;

		ArraySplit(int startIndex, int length) {
			this.startIndex = startIndex;
			this.length = length;
		}
	}

	// TaskD
	public static ArraySplit[] PartitionData(int length, int numPartitions) {
		ArraySplit[] threadWork = new ArraySplit[numPartitions];
		
		//number of partitions or length is zero
		if (numPartitions == 0 || length == 0) {
			System.out.println("Please input numbers bigger than zero for length and number of partitions");
			return null;
		}
		
		//the are more partitions than then length of the array allows
		if (length < numPartitions) {
			System.out.println("Length cannot be smaller than the number of partitions");
			return null;
		}
		
		//length divided by partition number results in round number, all partitions have equal length  
		if (length % numPartitions == 0) {
			int workLength = length / numPartitions;
			int startIndex = 0;
			for (int i = 0; i < threadWork.length; i++) {
				threadWork[i] = new ArraySplit(startIndex, workLength);
				startIndex += workLength;
			}
			return threadWork;
		
		//if length divided by partition number results in number with decimal, add 1 to the first partition length
		} else {
			int workLength = length / numPartitions;
			int startIndex = 0;
			threadWork[0] = new ArraySplit(0, workLength+1);
			startIndex += workLength+1;
			for (int i = 1; i < threadWork.length; i++) {
				threadWork[i] = new ArraySplit(startIndex, workLength);
				startIndex += workLength;
			}
			return threadWork;
		}
	}

	public static void taskA() {
		Thread t1 = new Thread(new ThreadThread("Thread1"));
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int[] taskB(final int[] values) {
		PrimeFactorsThread tuna = new PrimeFactorsThread(values);
		Thread t2 = new Thread(tuna);

		// calculate time needed for single thread
		long startTime = System.nanoTime();
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.nanoTime();
		long elapsedNs = endTime - startTime;
		double elapsedMs = elapsedNs / 1.0e6;
		System.out.println("Single thread time is " + elapsedMs + "ms");

		// calculate time needed for main thread
		long startTime2 = System.nanoTime();
		int[] salmon = computePrimeFactors(values);
		long endTime2 = System.nanoTime();
		long elapsedNs2 = endTime2 - startTime2;
		double elapsedMs2 = elapsedNs2 / 1.0e6;
		System.out.println("Main thread time is " + elapsedMs2 + "ms");

		// calculate time difference between main and single thread
		System.out.println("Delta is " + Math.abs(elapsedMs - elapsedMs2) + "ms");
		System.out.println();
		return tuna.getresult();
	}

	// Returns overhead of creating thread in nano-seconds
	public static long taskC() {
		long time_create = System.nanoTime();
		Thread t3 = new Thread(new EmptyThread());
		t3.start();
		try {
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		time_create = System.nanoTime() - time_create;
		return time_create;
	}

	public static int[] taskE(final int[] values, final int numThreads) {
		ArraySplit[] threadWork = PartitionData(values.length, numThreads);
		PrimeFactorsThread[] threads = new PrimeFactorsThread[numThreads];
		for (int i=0; i<2; i++) {
			threads[i] = new PrimeFactorsThread(Arrays.copyOfRange(values, threadWork[i].startIndex, threadWork[i].startIndex+threadWork[i].length-1));
			threads[i].start();
		}
		//TODO: merge threads into one
		
		throw new UnsupportedOperationException();
	}
}
