package TD1.EX11;

import TD1.EX11.model.SujetConcret;
import TD1.EX11.model.observer.NameObserver;
import TD1.EX11.model.observer.PriceObserver;

public class Main {
    public static void main(String[] args) {
        SujetConcret s = new SujetConcret("PopCorn", 1.29f);
        NameObserver nameObs = new NameObserver();
        PriceObserver priceObs = new PriceObserver();
        s.addObserver(nameObs);
        s.addObserver(priceObs);
        s.setName("Frosties");
        s.setPrice(4.575f);
        s.setPrice(9.22f);
        s.setName("Smacks");
    }
}