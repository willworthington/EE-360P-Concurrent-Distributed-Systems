// EID 1 = wjw692
// EID 2 = zbt86

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.*;


public class PriorityQueue {
	final ReentrantLock monitorLock = new ReentrantLock();
	final Condition notFull = monitorLock.newCondition();
	final Condition notEmpty = monitorLock.newCondition();

	final int maxSize;
	AtomicInteger size;
	AtomicReference<Node> head;

	// Creates a Priority queue with maximum allowed size as capacity
	// greatest to least priority
	public PriorityQueue(int maxSize) {
		this.maxSize = maxSize;
		size = new AtomicInteger(0);
		head = new AtomicReference<Node>(null);
	}

	// Adds the name with its priority to this queue.
	// Returns the current position in the list where the name was inserted;
	// otherwise, returns -1 if the name is already present in the list.
	// This method blocks when the list is full.
	public int add(String name, int priority) throws InterruptedException {
		// search for name and fail if found
		if(search(name) != -1)
			return -1;

		Node newNode = new Node(name, priority);
		int index = 0;


		// wait if full
		monitorLock.lock();
		while(size.get() == maxSize) {
			//System.out.println("Going to sleep");
			notFull.await();
			//System.out.println("Waking up");
		}
		monitorLock.unlock();

		synchronized (head) {
			while(head.get()!=null && !head.get().tryLock()) {
				//System.out.println("Dead");
			}
			
			// add to empty list
			if (head.get() == null) {
				head.set(newNode);

				monitorLock.lock();
				size.incrementAndGet();
				notEmpty.signal();
				monitorLock.unlock();

				return 0;
			}
			
			else if (newNode.priority>head.get().priority) {
				Node oldHead = head.get();
				newNode.next = oldHead;
				head.set(newNode);
				oldHead.unlock();
				
				monitorLock.lock();
				size.incrementAndGet();
				notEmpty.signal();
				monitorLock.unlock();
				
				return 0;
			}
			
			//head.get().lock();
		}
		
		

		Node cur = head.get();
		Node prev = null;


		// find location to insert
		while(cur != null && cur.priority >= newNode.priority){
			if(prev != null) {
				//System.out.println(index);
				prev.unlock();					//error
			}
			prev = cur;

			cur = cur.next;
			if(cur != null)
				cur.lock();

			index++;
		}


		// insert at end
		if(cur == null){
			prev.next = newNode;
			prev.unlock();
		}
		
		/*
		// insert at beginning
		else if(prev == null){
			synchronized (head) {
				newNode.next = cur;
				head.set(newNode);
				cur.unlock();
			}
		}*/

		// insert in middle
		else{
			newNode.next = cur;
			prev.next = newNode;
			cur.unlock();
			prev.unlock();			//error
		}

		monitorLock.lock();
		size.incrementAndGet();
		notEmpty.signal();
		monitorLock.unlock();

		return index;
	}

	// Returns the position of the name in the list;
	// otherwise, returns -1 if the name is not found.
	public int search(String name) {
		Node cur = head.get();
		int index = 0;

		// find index
		while(cur != null){
			cur.lock();
			if(cur.name.equals(name)) {
				cur.unlock();
				return index;
			}
			cur.unlock();
			cur = cur.next;
			index++;
		}

		return -1;
	}

	// Retrieves and removes the name with the highest priority in the list,
	// or blocks the thread if the list is empty.
	public String getFirst() {
		try {
			monitorLock.lock();

			//wait if empty
			while(size.get() == 0) {
				//monitorLock.lock();
				notEmpty.await();
				//monitorLock.unlock();
			}

			size.decrementAndGet();			// equal to --size

			//System.out.println(size);
			// why is head.get() null

			while(!head.get().tryLock()){}		// lock on most up to date head, instead of locking on a potential outdated one
			Node n = head.get();
			Node newHead = n.next;

			if(newHead != null)
				newHead.lock();

			head.set(newHead);

			if(newHead != null)
				newHead.unlock();
			n.unlock();

			//monitorLock.lock();
			notFull.signal();
			//monitorLock.unlock();

			//System.out.println(n.name);
			monitorLock.unlock();
			return n.name;

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "";
	}
}



class Node{
	final ReentrantLock nodeLock = new ReentrantLock();
	String name;
	int priority;
	Node next;

	public Node(String name, int priority){
		this.name = name;
		this.priority = priority;
		this.next = null;
	}

	public void lock(){
		nodeLock.lock();
	}

	public void unlock(){
		nodeLock.unlock();
	}
	
	public boolean tryLock() {
		return nodeLock.tryLock();
	}

}