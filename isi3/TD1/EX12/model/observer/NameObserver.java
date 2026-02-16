package TD1.EX12.model.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NameObserver implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("name".equals(evt.getPropertyName())) {
            System.out.println("Name: " + evt.getNewValue());
        }
    }
}
