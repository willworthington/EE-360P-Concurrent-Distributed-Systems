// EID 1 = wjw692
// EID 2 = zbt86

import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class CyclicBarrier {
	int parties;
	int count;
	CountingSemaphore countingSema;			// count threads waiting at gate
	CountingSemaphore gateSema;				// block threads, then release all when appropriate

	// count n while gate is closed
	// close counter
	// release n from gate
	// close gate
	// open counter


	public CyclicBarrier(int parties) {
		this.parties = parties;
		this.count = 0;
		countingSema = new CountingSemaphore(parties);		// close on n+1 thread
		gateSema = new CountingSemaphore(0);
	}
	
	public int await() throws InterruptedException {
		int index = parties-1-count;
		count++;

		// pass the counter until enough parties, will close on n+1 thread
		System.out.println("Stop at counter");
		countingSema.P();
		System.out.println("Pass counter");

		// last thread opens gate for n threads
		if(countingSema.getValue() == 0){
			for(int i=0; i<parties; i++){
				gateSema.V();
			}
		}

		// stop or pass at gate
		System.out.println("Stop at gate");
		gateSema.P();
		System.out.println("Pass gate");

		// last thread resets barrier
		if(countingSema.getValue() == 0) {
			// close gate
			gateSema.setValue(0);

			// open counter with n permits
			for(int i=0; i<parties; i++){
				countingSema.V();
			}

			// reset count
			count = 0;
		}

	    return index;
	}
}



class CountingSemaphore {
	int value;

	public CountingSemaphore(int initValue) {
		value = initValue;
	}
	public synchronized void P() throws InterruptedException {
		while (value == 0)
			try{
				wait();
			} catch(Exception e){
				throw e;
			}
		value--;
	}
	public synchronized void V() {
		value++;
		notify();
	}
	public synchronized int getValue(){
		return value;
	}
	public synchronized void setValue(int val){
		value = val;
	}
	public synchronized void openAll(){
		notifyAll();
	}
}