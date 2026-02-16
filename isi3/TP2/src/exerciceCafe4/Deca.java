package exerciceCafe4;

public class Deca extends DecoratorSupplement {
    public Deca() {
        super("Deca", 0.4);
    }

    @Override
    public void recette() {
        System.out.println("Preparation du Deca");
    }

}
