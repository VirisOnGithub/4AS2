package exerciceCafe4;

public class Sucre extends DecoratorSupplement {
    private static final double PRIX_SUCRE = 0.1;

    public Sucre(Boisson boisson) {
        super(boisson);
    }

    @Override
    public double getPrix() {
        return boisson.getPrix() + PRIX_SUCRE;
    }

    @Override
    public void recette() {
        boisson.recette();
        System.out.println("ajout de supplement : Sucre");
    }

    @Override
    public String toString() {
        return boisson.toString() + " avec supplement : Sucre (prix: " + PRIX_SUCRE + ")";
    }
}
