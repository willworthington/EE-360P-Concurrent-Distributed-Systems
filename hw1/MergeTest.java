import java.util.Arrays;
import java.util.Random;

public class MergeTest {

    public static void main(String[] args) {
        final int NUM_THREADS = 4; // Change this to however many threads you want to test with
        testRandom(NUM_THREADS);
        System.out.println("Congrats! Your implementation passes the randomly generated test cases!");
    }

    /**
     * Generates and tests PMerge 10 times with random array inputs.
     * @param numThreads number of threads to run PMerge with
     */
    public static void testRandom(int numThreads) {

        // Complex test case
        Random rand = new Random();
        for(int i = 0; i<500; ++i) {
            int aSize = rand.nextInt(20);
            int bSize = rand.nextInt(20);
            int[] a = new int[aSize], b = new int[bSize];
            for(int j = 0; j<a.length; ++j)
                a[j] = rand.nextInt(100);
            for(int j = 0; j<b.length; ++j)
                b[j] = rand.nextInt(100);
            int[] c = new  int[aSize + bSize];
            System.out.println("i " + i);
            System.out.println("c length " + c.length + ", a.length " + a.length + ", b length " + b.length);

            Arrays.sort(a);
            Arrays.sort(b);
            PMerge.parallelMerge(a, b, c, numThreads);
            verify(a, b, c);
        }


//    	// Medium test case
//        for(int i=0; i<100; i++) {
//        	int[] a = {1, 4, 10, 14, 16, 20, 40, 60, 80};
//        	//int[] b = {2};
//        	int[] b = {2, 4, 16, 18, 25, 35, 45, 80 };
//        	int[] c = new int[a.length+b.length];
//            PMerge.parallelMerge(a, b, c, numThreads);
//            verify(a, b, c);
//        }


//    	// Simple test case
//    	int[] a = {1,2,3,4};
//    	int[] b = {2,4,6};
//    	int[] c = new int[a.length+b.length];
//        PMerge.parallelMerge(a, b, c, numThreads);
//        verify(a, b, c);
    }

    static void verify(int[] a, int[] b, int[] c) {
        int[] expected = new int[a.length + b.length];
        System.arraycopy(a, 0, expected, 0, a.length);
        System.arraycopy(b, 0, expected, a.length, b.length);
        Arrays.sort(expected);
        reverse(expected);
        if(!Arrays.equals(expected, c)) {
            throw new AssertionError(String.format("\nExpected: %s\nReceived: %s\n", Arrays.toString(expected), Arrays.toString(c)));
        }
    }

    static void reverse(int[] a) {
        for(int i = 0; i<a.length/2; ++i) {
            a[i] ^= a[a.length-1-i];
            a[a.length-1-i] ^= a[i];
            a[i] ^= a[a.length-1-i];
        }
    }

}
