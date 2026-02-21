package beans;

import java.beans.PropertyChangeSupport;

public class YoutubeChannel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String videoId;

    public YoutubeChannel(String id) {
        this.videoId = id;
    }

    public void addSubscriber(Subscriber s) {
        pcs.addPropertyChangeListener(s);
    }

    public void notifySubscribers(){
        pcs.firePropertyChange("videoOut", "", this.videoId);
    }
}
