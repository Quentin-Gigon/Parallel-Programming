package assignment10.Bridge;

import java.util.Random;


public class BridgeMonitor extends Bridge {
	// TODO use this object as a monitor
	// you might find that you need some additional variables.

	static int carsOnBridge = 0;
	static boolean truckOnBridge = false;

	private final Object monitor = new Object(); 

	public void enterCar() throws InterruptedException {
		synchronized (monitor) {
			// TODO implement rules for car entry
			// If there are 3 cars or a truck on bridge, then car must wait
			while (carsOnBridge == 3 || truckOnBridge) {
				monitor.wait();
			}
			carsOnBridge++;
		}
	}

	public void leaveCar() {
		synchronized (monitor) {
			// TODO implement rules for car leave
			carsOnBridge--;
			monitor.notifyAll();
		}
	}

	public void enterTruck() throws InterruptedException {
		synchronized (monitor) {
			// TODO implement rules for truck entry - similar to car entry
			// If there are any cars or a truck on bridge, then truck must wait
			while (carsOnBridge > 0 || truckOnBridge) {
				monitor.wait();
			}
			truckOnBridge = true;
		}
	}

	public void leaveTruck() {
		synchronized (monitor) {
			// TODO implement rules for car leave - similar to car leave
			truckOnBridge = false;
			monitor.notifyAll();
		}
	}

	public static void main(String[] args) {
		Random r = new Random();
		BridgeMonitor b = new BridgeMonitor();
		for (int i = 0; i < 100; ++i) {
			if (r.nextBoolean()) {
				(new Car()).driveTo(b);
			} else {
				(new Truck()).driveTo(b);
			}
		}
	}

}
