package exerciceCafe4;

public class Colombia extends DecoratorSupplement {
    public Colombia() {
        super("Colombia", 0.5);
    }

    @Override
    public void recette() {
        System.out.println("Preparation du Colombia");
    }
}
