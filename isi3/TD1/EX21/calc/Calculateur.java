package TD1.EX21.calc;

import TD1.EX21.syncro.Syncronisateur;
import TD1.EX21.syncro.CalculateurSup;

public class Calculateur extends CalculateurSup {
    public Syncronisateur sync;

    public Calculateur(Syncronisateur sync) {
        this.sync = sync;
    }
}