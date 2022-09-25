//Question : Why can I not enclose line 51-55 in an else block?

package assignment13.stm;

import scala.concurrent.stm.Ref;
import scala.concurrent.stm.Ref.View;
import scala.concurrent.stm.TArray;
import scala.concurrent.stm.japi.STM;

import java.util.concurrent.Callable;

/**
 * This class implements a {@link assignment13.stm.CircularBuffer} using
 * software-transactional memory (more specifically using ScalaSTM
 * [http://nbronson.github.io/scala-stm/]).
 */
public class CircularBufferSTM<E> implements CircularBuffer<E> {

	TArray.View<E> items;
	Ref.View<Integer> count = STM.newRef(0);
	Ref.View<Integer> head = STM.newRef(0);
	Ref.View<Integer> tail = STM.newRef(0);

	CircularBufferSTM(int capacity) {
		// TODO: implement: create TArray of size capacity.
		this.items = STM.newTArray(capacity);
	}

	// TODO: implement: add item to the queue, otherwise block until a slot is free.
	public void put(final E item) {
		STM.atomic(new Runnable() {
			public void run() {
				if (isFull()) {
					STM.retry();
				} else {
					items.update(tail.get(), item);
					tail.set((tail.get() + 1) % items.length());
					STM.increment(count, 1);
				}
			}
		});
	}

	// TODO: implement: return the oldest item or block until an item is present.
	public E take() {
		E toTake = STM.atomic(new Callable<E>() {
			public E call() {
				if (isEmpty()) {
					STM.retry();
				}
				E toTake = items.refViews().apply(head.get()).get();
				items.update(head.get(), null);
				head.set((head.get() + 1) % items.length());
				STM.increment(count, -1);
				return toTake;
			}
		});
		return toTake;
	}

	public boolean isEmpty() {
		if (count.get() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFull() {
		// TODO: implement: helper method which can be used above.
		if (count.get() == items.length()) {
			return true;
		} else {
			return false;
		}
	}
}
