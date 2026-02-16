package strategycalculette;

public class Calculette {
    public float calculate(float a, float b, Operator op) {
        return op.calculate(a, b);
    }
}
