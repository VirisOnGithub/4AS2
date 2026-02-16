package exerciceCafe4;

import java.util.HashMap;
import java.util.function.Function;

public class SupplementFactory {
    private static SupplementFactory instance;
    private static HashMap<String, Function<Boisson, Boisson>> supplements;

    private SupplementFactory() {
        supplements = new HashMap<>();
        supplements.put("lait", Lait::new);
        supplements.put("sucre", Sucre::new);
        supplements.put("caramel", Caramel::new);
    }

    public static SupplementFactory getInstance() {
        if (instance == null) {
            instance = new SupplementFactory();
        }
        return instance;
    }

    public Boisson addSupplement(Boisson boisson, String typeSupplement) {
        if (typeSupplement == null || typeSupplement.isEmpty() || boisson == null) {
            return boisson;
        }

        if (typeSupplement.equals("fini")) {
            return boisson;
        }

        Function<Boisson, Boisson> constructor = supplements.get(typeSupplement);
        if (constructor == null) {
            System.out.println("Je n'ai pas pu rajouter " + typeSupplement);
            return boisson;
        }
        return constructor.apply(boisson);
    }
}
