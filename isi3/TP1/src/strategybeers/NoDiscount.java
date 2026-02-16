package strategybeers;

public class NoDiscount implements HappyHourStrategy {

    @Override
    public double applyDiscount(double price) {
        return price;
    }

}
