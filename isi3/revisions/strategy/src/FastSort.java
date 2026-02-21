import java.util.Arrays;

public class FastSort implements SortingStrategy{
    @Override
    public void sort(int[] array) {
        quicksort(array,0,array.length-1);
    }

    void quicksort(int[] array, int low, int high){
        //System.out.println("Quicksort: low=" +low+" high="+high+" array: "+ Arrays.toString(array));
        if (low < high){
            int pivot = array.length/2; // choix du pivot
            pivot = partition(array, low, high, pivot);
            quicksort(array, low, pivot - 1);
            quicksort(array, pivot + 1, high);
        }
    }

    int partition(int[] array, int low, int high, int pivot) {
        //System.out.println("Partition: low=" +low+" high="+high+" pivot="+pivot+" array: "+ Arrays.toString(array));
        swap(array, pivot, high);
        int j = low;
        for (int i = low; i < high; i++) {
            if (array[i] <= array[high]) {
                swap(array, i, j);
                j = j + 1;
            }
        }
        swap(array, high, j);
        return j;
    }

    void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
