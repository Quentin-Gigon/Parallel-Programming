//Comment1: I find it very confusing and unclear from the lecture slides what version of ReadWrite Lock we need to work with and how we need to use them... do we have to make our own (L18 slide 45), do we have to use the library(L18 slide 50? Something else? It's always said that the lecture slides are sufficient to understand the material and do the exercises but to me this is a clear example as to why it would make so much more sense to have explicit references to a textbook which explains the material more rigorously in additional to the slides... thats 's just my 2 cents.
//Comment2: Not sure why this doesn't work, followed the implementation from the lecture slides...(L18 slide 45) as well as a commented out version with the library (L18 slide 50) for which I had to refer to a textbook to learn how to use (If I'm not mistaken this appears nowhere in the lecture slides...)

package assignment11;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class LockedSensors implements Sensors {
	long time = 0;
	double data[];
	RWLock lk = new RWLock();
	ReadWriteLock lock = new ReentrantReadWriteLock();
	Lock r = lock.readLock();
	Lock w = lock.writeLock();

	class RWLock {
		int writers = 0;
		int readers = 0;

		synchronized void acquire_read() {
			while (writers > 0)
				try {
					wait();
				} catch (InterruptedException e) {
				}
			readers++;
		}

		synchronized void release_read() {
			readers--;
			notifyAll();
		}

		synchronized void acquire_write() {
			while (writers > 0 || readers > 0)
				try {
					wait();
				} catch (InterruptedException e) {
				}
			writers++;
		}

		synchronized void release_write() {
			writers--;
			notifyAll();
		}
	}

	LockedSensors() {
		time = 0;
	}

	// store data and timestamp
	// if and only if data stored previously is older (lower timestamp)
	public void update(long timestamp, double[] data) {
		// ADAPT ME I'M NOT THREADSAFE
		// w.lock();
		lk.acquire_write();
		;
		if (timestamp > time) {
			if (this.data == null) {
				this.data = new double[data.length];
			}
			time = timestamp;
			for (int i = 0; i < data.length; ++i)
				this.data[i] = data[i];
		}
		// w.unlock();
		lk.release_write();
	}

	// pre: val != null
	// pre: val.length matches length of data written via update
	// if no data has been written previously, return 0
	// otherwise return current timestamp and fill current data to array passed as
	// val
	public long get(double val[]) {
		// ADAPT ME I'M NOT THREADSAFE
		// r.lock();
		lk.acquire_read();
		if (time == 0) {
			lk.release_read();
			return 0;
		} else {
			for (int i = 0; i < data.length; ++i) {
				val[i] = data[i];
			}
			// r.unlock();
			lk.release_read();
			return time;
		}
	}
}
