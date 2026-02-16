package exerciceCafe1;

public class Deca extends Boisson {
    public Deca() {
        super("Deca", 0.4);
    }

    @Override
    public void recette() {
        System.out.println("Preparation du Deca");
    }
    
}
