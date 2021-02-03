//UT-EID= wjw692, zbt86


import java.util.concurrent.*;

public class PSort  {
  public static void parallelSort(int[] A, int begin, int end){
    // TODO: Implement your parallel sort function
    int processors = Runtime.getRuntime().availableProcessors();
    //System.out.println("Number of processors: " + processors);

    SortTask s = new SortTask(A, begin, end-1);

    ForkJoinPool pool = new ForkJoinPool(processors);
    pool.invoke(s);

  }
}


class SortTask extends RecursiveTask<Void> {
  int[] arr;
  int low;
  int high;

  SortTask(int[] arr, int low, int high) {
    this.arr = arr;
    this.low = low;
    this.high = high;
  }

  protected Void compute() {
    //  If at any stage, the
    //array size is less than or equal to 16, then the simple sequential insert sort is used for sorting
    if((high - low + 1) <= 16){
      insertionSort(arr);
    }

    // Quick sort
    else if (low < high)
    {
      /* pi is partitioning index, arr[pi] is now
         at right place */
      int pi = partition(arr, low, high);

      SortTask s1 = new SortTask(arr, low, pi-1);
      s1.fork();

      SortTask s2 = new SortTask(arr, pi+1, high);
      s2.compute();
      s1.join();

    }

    return null;
  }

  private int partition (int arr[], int low, int high)
  {
    // pivot (Element to be placed at right position)
    int pivot = arr[high];

    int i = (low - 1);  // Index of smaller element

    for (int j = low; j <= high- 1; j++)
    {
      // If current element is smaller than the pivot
      if (arr[j] < pivot)
      {
        i++;    // increment index of smaller element
        swap(arr, i, j);
      }
    }
    swap(arr, i+1, high);
    return (i + 1);
  }

  private void swap(int arr[], int a, int b)
  {
    int temp = arr[a];
    arr[a] = arr[b];
    arr[b] = temp;
  }

  private void insertionSort(int arr[]){
    int n = arr.length;
    for (int i = 1; i < n; ++i) {
      int key = arr[i];
      int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }
      arr[j + 1] = key;
    }
  }

}


