package exerciceCafe3;

public abstract class DecoratorSupplement extends Boisson {
    protected Boisson boisson;

    public DecoratorSupplement(Boisson boisson) {
        super(boisson.getNom(), boisson.getPrix());
        this.boisson = boisson;
    }

    public DecoratorSupplement(String name, double prix) {
        super(name, prix);
    }

    @Override
    public abstract void recette();
}
