package etatcalculette;

public class EtatSoustraction implements EtatCalculette {
    @Override
    public float calculate(float a, float b) {
        return a - b;
    }
}
