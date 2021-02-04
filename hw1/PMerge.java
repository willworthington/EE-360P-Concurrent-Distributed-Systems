//UT-EID= wjw692, zbt86


import java.util.*;
import java.util.concurrent.*;


public class PMerge{
	public static void parallelMerge(int[] A, int[] B, int[]C, int numThreads){
	    try {
			ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);

			for(int i=0; i<A.length; i++) {
				threadPool.submit(new InsertionTask(A, B, C, i));
			}
			for(int i=0; i<B.length; i++) {
				threadPool.submit(new InsertionTask(B, A, C, i));
			}

			threadPool.shutdown();
			threadPool.awaitTermination(1, TimeUnit.MINUTES);

		} catch (Exception e) { System.err.println (e); }
	}
}

class InsertionTask implements Callable<Void> {
	private int[] A;
	private int[] B;
	private int[] C;
	private int idx;
	
	public InsertionTask(int[] A, int[] B, int[] C, int idx) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.idx = idx;
	}
	
	public Void call() {
		int idx2 = 0;
		// do binary search in B to find index of (idx2) A[idx] in B
		// don't binary search if B's length is 0.  It'll cause index out of bounds
		if (B.length != 0) {
			idx2 = binarySearch(B, A[idx]);
		}
		//System.out.println(A[idx] + " " + idx + " " + idx2 + " " + (idx + idx2));

		// set C[idx + idx2] = A[idx];
		C[C.length - 1 - (idx + idx2)] = A[idx];
		
		// if B[idx2] == A[idx] then there's a duplicate
		// if duplicate, then set C[idx + idx2 + 1] = A[idx]
		if(idx2<B.length && B[idx2] == A[idx]) {
			C[C.length -1 - (idx + idx2 + 1)] = A[idx];
		}
		
		
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