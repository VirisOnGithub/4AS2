package exerciceCafe4;

public class Lait extends DecoratorSupplement {
    private static final double PRIX_LAIT = 0.3;

    public Lait(Boisson boisson) {
        super(boisson);
    }

    @Override
    public double getPrix() {
        return boisson.getPrix() + PRIX_LAIT;
    }

    @Override
    public void recette() {
        boisson.recette();
        System.out.println("ajout de supplement : Lait");
    }

    @Override
    public String toString() {
        return boisson.toString() + " avec supplement : Lait (prix: " + PRIX_LAIT + ")";
    }
}
