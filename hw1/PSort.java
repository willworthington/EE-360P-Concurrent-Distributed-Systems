//UT-EID=


import java.util.*;
import java.util.concurrent.*;

public class PSort{
  public static void parallelSort(int[] A, int begin, int end){
    // TODO: Implement your parallel sort function
    quickSort(A, begin, end-1);
  }

  /* low  --> Starting index,  high  --> Ending index */
  public static void quickSort(int arr[], int low, int high)
  {
    if (low < high)
    {
        /* pi is partitioning index, arr[pi] is now
           at right place */
      int pi = partition(arr, low, high);

      quickSort(arr, low, pi - 1);  // Before pi
      quickSort(arr, pi + 1, high); // After pi
    }
  }

  /* This function takes last element as pivot, places
   the pivot element at its correct position in sorted
    array, and places all smaller (smaller than pivot)
   to left of pivot and all greater elements to right
   of pivot */
  public static int partition (int arr[], int low, int high)
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

  public static void swap(int arr[], int a, int b)
  {
    int temp = arr[a];
    arr[a] = arr[b];
    arr[b] = temp;
  }
}


