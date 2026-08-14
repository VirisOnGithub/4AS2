public class Cercle implements Forme {
    double rayon;
    Cercle(double rayon) { this.rayon = rayon; }

    @Override
    public void accept(Visiteur v) {
        v.visit(this);
    }
}