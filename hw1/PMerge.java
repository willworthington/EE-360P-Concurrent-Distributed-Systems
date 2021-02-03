//UT-EID= wjw692, zbt86


import java.util.*;
import java.util.concurrent.*;


public class PMerge{
	public static void parallelMerge(int[] A, int[] B, int[]C, int numThreads){
	    try {
			ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
			CountDownLatch latch = new CountDownLatch(A.length + B.length - 1);

			for(int i=0; i<A.length; i++) {
				threadPool.submit(new InsertionTask(A, B, C, i, latch));
			}
			for(int i=0; i<B.length; i++) {
				threadPool.submit(new InsertionTask(B, A, C, i, latch));
			}

			threadPool.shutdown();
			threadPool.awaitTermination(1, TimeUnit.MINUTES);
			threadPool.shutdownNow();

			//while(latch.getCount() > 0)
				//System.out.println("latch count " + latch.getCount());
			//latch.await();

		} catch (Exception e) { System.err.println (e); }
	}
}

class InsertionTask implements Callable<Void> {
	private int[] A;
	private int[] B;
	private int[] C;
	private int idx;
	private CountDownLatch latch;
	
	public InsertionTask(int[] A, int[] B, int[] C, int idx, CountDownLatch latch) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.idx = idx;
		this.latch = latch;
	}
	
	public Void call() {
		// do binary search in B to find index of (idx2) A[idx] in B
		int idx2 = binarySearch(B, A[idx]);
		System.out.println(A[idx] + " " + idx + " " + idx2 + " " + (idx + idx2));

		// set C[idx + idx2] = A[idx];
		C[C.length - 1 - (idx + idx2)] = A[idx];
		//C[idx + idx2] = A[idx];
		
		// if B[idx2] == A[idx] then there's a duplicate
		// if duplicate, then set C[idx + idx2 + 1] = A[idx]
		if(B[idx2] == A[idx]) {
			C[C.length -1 - (idx + idx2 + 1)] = A[idx];
			//C[idx + idx2 + 1] = A[idx];
		}

		latch.countDown();
		return null;
	}
	
	private int binarySearch(int[] arr, int target) {
		int l = 0;
		int r = arr.length-1;
		int i=0;
		while(l<=r) {
			i = (l + r) / 2;
			if (arr[i] == target) {
				return i;
			}
			else if (arr[i] < target) {
				l = i + 1;
			}
			else {
				r = i - 1;
			}
		}
		if(arr[i] < target)
			i++;

		return i;
	}
}