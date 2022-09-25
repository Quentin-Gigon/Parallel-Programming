package ethz.ch.pp.assignment3.counters;

//TODO: implement
public class SequentialCounter implements Counter {
	int value;

	@Override
	public void increment() {
		value++;
	}

	@Override
	public int value() {
		return value;
	}
}