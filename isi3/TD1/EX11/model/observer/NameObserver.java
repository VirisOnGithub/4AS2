package TD1.EX11.model.observer;

public class NameObserver implements java.util.Observer {
    @Override
    public void update(java.util.Observable o, Object arg) {
        if (arg instanceof String) {
            System.out.println("Name: " + arg);
        }
    }
}
