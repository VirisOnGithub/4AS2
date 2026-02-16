package TD1.EX11.model.observer;

public class PriceObserver implements java.util.Observer {
    @Override
    public void update(java.util.Observable o, Object arg) {
        if (arg instanceof Float) {
            System.out.println("Price: " + arg);
        }
    }
}
