import java.util.Random;

public class testPriorityQueue implements Runnable {
	final static int SIZE = 3;
	final static int queueSize = 3;

	final PriorityQueue queue;
	int priority;
	
	public testPriorityQueue(PriorityQueue queue, int priority) {
		this.queue = queue;
		this.priority = priority;
	}
	
	public void run() {
		try {
			//System.out.println("Trying to add thread " + Thread.currentThread().getId() + " with priority " + priority);
			queue.add("Thread " + Thread.currentThread().getId(), priority);
			//System.out.println("Added thread " + Thread.currentThread().getId() + " with priority " + priority);

			Thread.sleep(500);
			//System.out.println("First popped is " + queue.getFirst());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PriorityQueue queue = new PriorityQueue(queueSize);
		Random random = new Random();

		Thread[] t = new Thread[SIZE];
		int[] priorities = {5, 9, 7};

		for (int i = 0; i < SIZE; ++i) {
			//t[i] = new Thread(new testPriorityQueue(queue, random.nextInt(10)));
			t[i] = new Thread(new testPriorityQueue(queue, priorities[i]));
		}
		
		for (int i = 0; i < SIZE; ++i) {
			t[i].start();
		}
    }
}

