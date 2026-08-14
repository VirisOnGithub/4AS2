public class Triangle implements Forme {
    double a, b, c; // les 3 côtés
    Triangle(double a, double b, double c) {
        this.a = a; this.b = b; this.c = c;
    }

    @Override
    public void accept(Visiteur v) {
        v.visit(this);
    }
}