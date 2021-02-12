/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class CyclicBarrier {
	
	public CyclicBarrier(int parties) {
	}
	
	public int await() throws InterruptedException {
           int index = 0;
		
          // you need to write this code
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
				this.wait();
			} catch(Exception e){
				throw e;
			}
		value--;

	}
	public synchronized void V() {
		value++;
		notify();
	}
}