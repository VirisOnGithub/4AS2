package TD1.EX12.model.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PriceObserver implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("price".equals(evt.getPropertyName())) {
            System.out.println("Price: " + evt.getNewValue());
        }
    }
}
