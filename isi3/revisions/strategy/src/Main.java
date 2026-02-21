import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class Main {
    static void main() {
        int SIZE = 100000000;

        Random rand = new Random();
        int[] arr = new int[SIZE];
        for (int i = 0; i < SIZE; i++){
            arr[i] = rand.nextInt(SIZE);
        }

        long start = System.nanoTime();
        Sorter s = new Sorter(new FastSort());
        s.sort(arr.clone());
        System.out.println("Sorted in " + (System.nanoTime() - start) / 1_000_000 + " ms");

//        start = System.nanoTime();
//        s = new Sorter(new InsertionSort());
//        s.sort(arr.clone());
//        System.out.println("Sorted in " + (System.nanoTime() - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        s = new Sorter(new MergeSort());
        s.sort(arr.clone());
        System.out.println("Sorted in " + (System.nanoTime() - start) / 1_000_000 + " ms");
        //System.out.println(Arrays.toString(arr));
    }
}