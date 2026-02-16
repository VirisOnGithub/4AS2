package exerciceCafe1;

public class Expresso extends Boisson {
    public Expresso() {
        super("Expresso", 0.6);
    }

    @Override
    public void recette() {
        System.out.println("Preparation de l'Expresso");
    }
    
}
