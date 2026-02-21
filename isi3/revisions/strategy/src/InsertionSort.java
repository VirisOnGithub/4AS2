import java.util.Arrays;

public class InsertionSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {
        int l = array.length;
        for (int i = 1; i < l; i++) {
//            System.out.println("Iteration " + i + ": " + Arrays.toString(array));
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;

        }
    }
}
