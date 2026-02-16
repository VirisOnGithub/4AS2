package TD1.EX21.syncro;

import java.util.List;

public class Syncronisateur {
    List<CalculateurSup> calc;

    public Syncronisateur() {
        this.calc = new java.util.ArrayList<>();
    }

    public void addCalculateur(CalculateurSup calculateur) {
        this.calc.add(calculateur);
    }

    public void calculer() {
        calc.forEach(CalculateurSup::calculer);
    }
}