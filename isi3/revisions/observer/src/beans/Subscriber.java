package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Subscriber implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("videoOut")) {
            System.out.println("Nouvelle vidéo sortie ! https://www.youtube.com/watch?v="+evt.getNewValue());
        }
    }
}
