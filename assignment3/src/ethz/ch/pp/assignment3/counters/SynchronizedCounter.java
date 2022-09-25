package ethz.ch.pp.assignment3.counters;

//TODO: implement
public class SynchronizedCounter implements Counter {
	int value;
	
	@Override
	public synchronized void increment() {
		value++;			
	}

	@Override
	public synchronized int value() {
		return value;		
	}

}