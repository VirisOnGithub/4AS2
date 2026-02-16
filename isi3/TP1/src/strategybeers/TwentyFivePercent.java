package strategybeers;

public class TwentyFivePercent implements HappyHourStrategy {

    @Override
    public double applyDiscount(double price) {
        return price * 0.75;
    }

}
