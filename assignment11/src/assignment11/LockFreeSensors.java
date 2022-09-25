//Comment: Not very sure why my implementation doesn't seem to work. The lecture slides provide very little guidance and clarity about how to use AtomicReference imo, basically same comment in line with Comment1 in the LockedSensors.java...

package assignment11;

import java.util.concurrent.atomic.AtomicReference;

class LockFreeSensors implements Sensors {

	AtomicReference<SensorData> sd = new AtomicReference<SensorData>(new SensorData(0, null));

	LockFreeSensors() {
		sd.set(new SensorData(0, null));
	}

	// store data and timestamp
	// if and only if data stored previously is older (lower timestamp)
	public void update(long timestamp, double[] data) {
		// IMPLEMENT ME

		// make this lock-free
		// don't try (a variant) double compare and swap
		// hint: make use of the SensorData class (why?)

		if (timestamp > sd.get().timestamp) {
			double[] dataNew = new double[data.length];
			for (int i = 0; i < data.length; ++i) {
				dataNew[i] = data[i];
			}
			SensorData sensorDataNew = new SensorData(timestamp, dataNew);
			sd.compareAndSet(sd.get(), sensorDataNew);
		}
	}

	// pre: val != null
	// pre: val.length matches length of data written via update
	// if no data has been written previously, return 0
	// otherwise return current timestamp and fill current data to array passed as
	// val
	public long get(double val[]) {
		// ADAPT ME I'M NOT THREADSAFE
		if (sd.get().timestamp == 0) {
			return 0;
		} else {
			for (int i = 0; i < sd.get().data.length; ++i) {
				val[i] = sd.get().data[i];

			}
			return sd.get().timestamp;
		}
	}

}
