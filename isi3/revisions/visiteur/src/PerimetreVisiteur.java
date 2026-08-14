public class PerimetreVisiteur implements Visiteur {
    @Override
    public void visit(Cercle c) {
        double perimetre = 2 * Math.PI * c.rayon;
        System.out.println("Périmètre du cercle : " + perimetre);
    }

    @Override
    public void visit(Rectangle r) {
        double perimetre = 2 * (r.largeur + r.hauteur);
        System.out.println("Périmètre du rectangle : " + perimetre);
    }

    @Override
    public void visit(Triangle t) {
        double perimetre = t.a + t.b + t.c;
        System.out.println("Périmètre du triangle : " + perimetre);
    }
}