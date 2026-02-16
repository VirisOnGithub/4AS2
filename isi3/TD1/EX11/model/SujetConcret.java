package TD1.EX11.model;

import java.util.Observable;

public class SujetConcret extends Observable {
    private String name;
    private float price;

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
        this.price = price;
        setChanged();
        notifyObservers(price);
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers(name);
    }
}
