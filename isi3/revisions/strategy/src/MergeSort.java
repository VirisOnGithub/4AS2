import java.util.Arrays;

public class MergeSort implements SortingStrategy {
    @Override
    public void sort(int[] array) {

    }

    int[] mergeSort(int[] array) {
        int n = array.length;
        if (n <= 1) {
            return array;
        } else {
            int mid = n / 2;
            int[] left = Arrays.copyOfRange(array, 0, mid);
            int[] right = Arrays.copyOfRange(array, mid, n);
            return interclass(left, right);
        }
    }

    int[] interclass(int[] a, int[] b) {
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        int lenA = a.length;
        int lenB = b.length;

        int[] res = new int[lenA + lenB];

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < lenA && j < lenB) {
            if (a[i] < b[j]) {
                res[k++] = a[i++];
            } else {
                res[k++] = b[j++];
            }
        }

        while (i < lenA) {
            res[k++] = a[i++];
        }
        while (j < lenB) {
            res[k++] = b[j++];
        }

        return res;
    }
}
