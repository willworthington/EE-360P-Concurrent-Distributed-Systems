// EID 1 = wjw692
// EID 2 = zbt86

import java.util.concurrent.Semaphore; // for implementation using Semaphores
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrier {
	int parties;
	AtomicInteger count;
	Semaphore countingSema;			// count threads waiting at gate
	Semaphore gateSema;				// block threads, then release all when appropriate

	// count n while gate is closed
	// close counter
	// release n from gate
	// close gate
	// open counter


	public CyclicBarrier(int parties) {
		this.parties = parties;
		this.count = new AtomicInteger();
		countingSema = new Semaphore(parties, true);
		gateSema = new Semaphore(0, true);
	}
	
	public int await() throws InterruptedException {
		int index = parties-1-count.get();
		//count++;

		// pass the counter until enough parties, will close on n+1 thread
		countingSema.acquire();

		// last thread opens gate for n threads
		if(countingSema.availablePermits() == 0) {
			gateSema.release(parties-1);	// release all preceding threads at gate
			count.set(0);
			countingSema.release(parties);  // reset countingSema
		}
		else {
			count.getAndIncrement();
			// stop at gate until N'th thread arrives and wakes all up
			gateSema.acquire();
		}
		
		// not sure index will be valid unless we make count an atomic integer
	    return index;
	}
}
