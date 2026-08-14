package metier;

import java.util.concurrent.atomic.AtomicInteger;

public class Stuff {
    private final AtomicInteger counter;

    public Stuff() {
        this.counter = new AtomicInteger(0);
    }

    public int incrementAndGet() {
        return counter.incrementAndGet();
    }

    public String buildMessage(int value) {
        return "C'est le " + value + "eme Hello World !";
    }
}
