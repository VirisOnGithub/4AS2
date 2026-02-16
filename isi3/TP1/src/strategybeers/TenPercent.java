package strategybeers;

public class TenPercent implements HappyHourStrategy {

    @Override
    public double applyDiscount(double price) {
        return price * 0.9;
    }

}
