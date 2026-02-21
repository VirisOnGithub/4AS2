public class Reader {
    private ReaderState state = null;

    public Reader(ReaderState state) {
        this.state = state;
    }

    public void pressButton() {
        this.state.pressButton(this);
    }

    public void setState(ReaderState state) {
        this.state = state;
    }
}
