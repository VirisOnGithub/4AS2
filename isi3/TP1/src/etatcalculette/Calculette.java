package etatcalculette;

public class Calculette {
    private EtatCalculette etat;

    public void setEtat(EtatCalculette etat) {
        this.etat = etat;
    }

    public float calculate(float a, float b) {
        if (etat == null) {
            throw new IllegalStateException("Aucun état défini pour la calculette.");
        }
        return etat.calculate(a, b);
    }
}
