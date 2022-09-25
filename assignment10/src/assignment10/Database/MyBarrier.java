// Honestly I tried for hours to implement a "barrier using a monitor" but I just don't see how we are supposed 
//to know how to do that based on the resources in the lecture notes... all I see is some slides about how to 
//implement a monitor, and some slides about how to implement a two phase barrier using semaphores, which somehow 
//also doesn't seem to work when I implement it exactly like in the lecture notes (see slide 37 L17)

package assignment10.Database;

import java.util.concurrent.Semaphore;

public class MyBarrier {
	MySemaphore mutex = new MySemaphore(1);
	MySemaphore barrier1 = new MySemaphore(0);
	MySemaphore barrier2 = new MySemaphore(1);
	int threshhold;
	int count = 0;

	MyBarrier(int n) {
		// TODO find suitable variables for the barrier and initialize them
		this.threshhold = n;
	}

	synchronized void await() {
		try {
			mutex.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO implement the barrier await using Monitors
		count++;
		if (count == threshhold) {
			try {
				barrier2.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			barrier1.release();
		}
		mutex.release();

		try {
			barrier1.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		barrier1.release();

		try {
			mutex.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		count--;
		if (count == 0) {
			try {
				barrier1.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			barrier2.release();
		}
		mutex.notifyAll();
		

		try {
			barrier2.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		barrier2.release();
	}
}
