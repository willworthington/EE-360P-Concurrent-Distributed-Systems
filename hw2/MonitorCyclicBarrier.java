// EID 1 = wjw692
// EID 2 = zbt86

import java.util.concurrent.atomic.AtomicInteger;

public class MonitorCyclicBarrier {
	int parties;
	AtomicInteger count;

	public MonitorCyclicBarrier(int parties) {
		this.parties = parties;
		this.count = new AtomicInteger(0);
	}
	
	public synchronized int await() throws InterruptedException {
		int index = parties-1-count.get();

		// perform task
		count.getAndIncrement();

		// wake others
		if(index == 0){
			notifyAll();
			count.set(0);
			return index;
		}

		// check and wait
		if(count.get() < parties){
			wait();
		}

		return index;
	}
}
