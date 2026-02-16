package exerciceCafe1;

public class Colombia extends Boisson {
    public Colombia() {
        super("Colombia", 0.5);
    }

    @Override
    public void recette() {
        System.out.println("Preparation du Colombia");
    }
    
}
