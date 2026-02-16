package exerciceCafe4;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BoissonFactory {
    private static BoissonFactory factory;

    private static final Map<String, Supplier<Boisson>> BOISSONS = new HashMap<>();

    static {
        BOISSONS.put("deca", Deca::new);
        BOISSONS.put("expresso", Expresso::new);
        BOISSONS.put("colombia", Colombia::new);
    }

    public static BoissonFactory getInstance() {
        if (factory == null) {
            factory = new BoissonFactory();
        }
        return factory;
    }

    public Boisson createBoisson(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }

        Supplier<Boisson> supplier = BOISSONS.get(type.toLowerCase());
        return supplier != null ? supplier.get() : null;
    }
}
