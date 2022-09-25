package ethz.ch.pp.assignment3.counters;

import java.util.concurrent.atomic.AtomicInteger;

//TODO: implement
public class AtomicCounter implements Counter {
	AtomicInteger value = new AtomicInteger();

	@Override
	public void increment() {
		value.getAndIncrement();
	}

	@Override
	public int value() {
		//TODO: implement
		int toreturn = value.intValue();
		return toreturn;		
	}

}