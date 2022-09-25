package assignment10.Database;

public class MySemaphore {

	private volatile int count;
	Object monitor = new Object();

	public MySemaphore(int maxCount) {
		// TODO initialize count suitably
		this.count = maxCount;

	}

	public void acquire() throws InterruptedException {
		// TODO implement suitable monitor and implement semaphore acquisition
		synchronized (monitor) {
			while (count == 0) {
				monitor.wait();
			}
			count--;
		}
	}

	public void release() {
		// TODO implement suitable monitor and implement semaphore release
		synchronized (monitor) {
			count++;
		}
	}

}
