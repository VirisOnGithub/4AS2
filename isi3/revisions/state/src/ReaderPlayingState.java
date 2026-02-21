public class ReaderPlayingState implements ReaderState {

    @Override
    public void pressButton(Reader r) {
        System.out.println("Reader paused");
        r.setState(new ReaderPausedState());
    }
}
