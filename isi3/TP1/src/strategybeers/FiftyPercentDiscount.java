package strategybeers;

public class FiftyPercentDiscount implements HappyHourStrategy {
    @Override
    public double applyDiscount(double price) {
        return price * 0.5;
    }

}