public class AireVisiteur implements Visiteur {
    @Override
    public void visit(Cercle c) {
        double aire = Math.PI * c.rayon * c.rayon;
        System.out.println("Aire du cercle : " + aire);
    }

    @Override
    public void visit(Rectangle r) {
        double aire = r.largeur * r.hauteur;
        System.out.println("Aire du rectangle : " + aire);
    }

    @Override
    public void visit(Triangle t) {
        double s = (t.a + t.b + t.c) / 2; // demi-périmètre
        double aire = Math.sqrt(s * (s - t.a) * (s - t.b) * (s - t.c)); // formule de Héron
        System.out.println("Aire du triangle : " + aire);
    }
}
