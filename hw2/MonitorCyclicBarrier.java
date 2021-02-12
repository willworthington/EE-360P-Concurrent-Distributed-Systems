/*
 * EID's of group members
 * 
 */

public class MonitorCyclicBarrier {
	int parties;
	int count;

	public MonitorCyclicBarrier(int parties) {
		this.parties = parties;
		this.count = 0;
	}
	
	public synchronized int await() throws InterruptedException {
		int index = parties-1-count;

		// perform task
		count++;

		// wake others
		if(index == 0){
			notifyAll();
			count = 0;
			return index;
		}

		// check and wait
		if(count < parties){
			wait();
		}

		return index;
	}
}
