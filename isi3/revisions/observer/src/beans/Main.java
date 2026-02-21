package beans;

public class Main {
    static void main() {
        YoutubeChannel youtubeChannel = new YoutubeChannel("C-EIrVM4tZU");
        for (int i = 0; i < 3; i++) {
            Subscriber s = new Subscriber();
            youtubeChannel.addSubscriber(s);
        }

        youtubeChannel.notifySubscribers();
    }
}