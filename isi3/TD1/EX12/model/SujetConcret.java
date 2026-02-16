package TD1.EX12.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SujetConcret {
    private String name;
    private float price;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SujetConcret(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        float oldPrice = this.price;
        this.price = price;
        pcs.firePropertyChange("price", oldPrice, price);
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        pcs.firePropertyChange("name", oldName, name);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
