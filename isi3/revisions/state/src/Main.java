public class Main {
    static void main() throws InterruptedException {
        Reader r = new Reader(new ReaderPausedState());
        for (int i = 0; i < 5; i++) {
            r.pressButton();
            Thread.sleep(1000);
        }
    }
}
