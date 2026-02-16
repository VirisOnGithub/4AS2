package exerciceCafe3;

public class Caramel extends DecoratorSupplement {
    private static final double PRIX_CARAMEL = 0.5;

    public Caramel(Boisson boisson) {
        super(boisson);
    }

    @Override
    public double getPrix() {
        return boisson.getPrix() + PRIX_CARAMEL;
    }

    @Override
    public void recette() {
        boisson.recette();
        System.out.println("ajout de supplement : Caramel");
    }

    @Override
    public String toString() {
        return boisson.toString() + " avec supplément Caramel (prix: " + PRIX_CARAMEL + ")";
    }
}
