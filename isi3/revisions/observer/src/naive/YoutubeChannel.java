package naive;

import java.util.ArrayList;
import java.util.List;

public class YoutubeChannel {
    private List<Subscriber> subscribers = new ArrayList<>();
    private String video;

    public YoutubeChannel(String video) {
        this.video = video;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(this.video);
        }
    }
}
