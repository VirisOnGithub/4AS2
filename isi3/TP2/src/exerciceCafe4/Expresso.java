package exerciceCafe4;

public class Expresso extends DecoratorSupplement {
    public Expresso() {
        super("Expresso", 0.6);
    }

    @Override
    public void recette() {
        System.out.println("Preparation de l'Expresso");
    }

}
