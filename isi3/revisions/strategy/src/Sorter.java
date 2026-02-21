public class Sorter {
    SortingStrategy strategy;
    public Sorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(int[] array) {
        this.strategy.sort(array);
    }
}
