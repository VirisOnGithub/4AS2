package TD1.EX21.calc;

import TD1.EX21.syncro.Syncronisateur;
import TD1.EX21.syncro.CalculateurSup;
import TD1.EX21.barre.Barre;

public class CalculateurProg extends CalculateurSup {
    public Syncronisateur sync;
    public Barre barre;

    public CalculateurProg(Syncronisateur sync) {
        this.sync = sync;
    }

    public void declareBarre(Barre barre) {
        this.barre = barre;
    }

    public int getAvancement() {
        return 3;
    }
}