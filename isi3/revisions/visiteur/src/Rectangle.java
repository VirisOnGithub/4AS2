public class Rectangle implements Forme {
    double largeur, hauteur;
    Rectangle(double largeur, double hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    @Override
    public void accept(Visiteur v) {
        v.visit(this);
    }
}