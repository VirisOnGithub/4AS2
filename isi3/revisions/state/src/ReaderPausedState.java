public class ReaderPausedState implements ReaderState {

    @Override
    public void pressButton(Reader r) {
        System.out.println("Reader playing");
        r.setState(new ReaderPlayingState());
    }
}
