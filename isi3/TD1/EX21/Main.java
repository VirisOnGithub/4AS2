package TD1.EX21;

import TD1.EX21.barre.Barre;
import TD1.EX21.calc.CalculateurProg;
import TD1.EX21.syncro.Syncronisateur;

public class Main {
    public static void main(String[] args) {
        Syncronisateur sync = new Syncronisateur();
        CalculateurProg calc = new CalculateurProg(sync);
        sync.addCalculateur(calc);
        Barre barre = new Barre(calc);
        calc.declareBarre(barre);
        calc.barre.avancement();
    }
}
