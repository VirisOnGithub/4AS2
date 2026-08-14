import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Forme> formes = List.of(
                new Cercle(5),
                new Rectangle(4, 6),
                new Triangle(3, 4, 5)
        );

        Visiteur aire = new AireVisiteur();
        Visiteur perimetre = new PerimetreVisiteur();

        for (Forme f : formes) {
            f.accept(aire);
            f.accept(perimetre);
        }
    }

}
