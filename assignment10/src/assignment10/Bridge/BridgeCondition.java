package assignment10.Bridge;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BridgeCondition extends Bridge {

	// TODO use this given lock and create conditions form it
	// you might find that you need some additional variables
	final Lock bridgeLock = new ReentrantLock();
	final Condition vehicleCanEnter = bridgeLock.newCondition();

	static int carsOnBridge = 0;
	static boolean truckOnBridge = false;

	public void enterCar() throws InterruptedException {
		bridgeLock.lock();
		while (carsOnBridge == 3 || truckOnBridge) {
			vehicleCanEnter.await();
		}
		carsOnBridge++;
		bridgeLock.unlock();
	}

	public synchronized void leaveCar() {
		// TODO implement rules for car leave
		bridgeLock.lock();
		carsOnBridge--;
		vehicleCanEnter.signalAll();
		bridgeLock.unlock();
	}

	public void enterTruck() throws InterruptedException {
		// TODO implement rules for truck entry - similar to car entry
		bridgeLock.lock();
		while (carsOnBridge > 0 || truckOnBridge) {
			vehicleCanEnter.await();
		}
		truckOnBridge = true;
		bridgeLock.unlock();
	}

	public void leaveTruck() {
		// TODO implement rules for car leave - similar to car leave
		bridgeLock.lock();
		truckOnBridge = false;
		vehicleCanEnter.signalAll();
		bridgeLock.unlock();
	}

	public static void main(String[] args) {
		Random r = new Random();
		BridgeCondition b = new BridgeCondition();
		for (int i = 0; i < 100; ++i) {
			if (r.nextBoolean()) {
				(new Car()).driveTo(b);
			} else {
				(new Truck()).driveTo(b);
			}
		}
	}

}
