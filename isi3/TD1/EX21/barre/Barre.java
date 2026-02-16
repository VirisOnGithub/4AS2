package TD1.EX21.barre;

import TD1.EX21.calc.CalculateurProg;

public class Barre implements BarreSup {

    public CalculateurProg prog;

    public Barre(CalculateurProg prog) {
        this.prog = prog;
    }

    @Override
    public void avancement() {
        System.out.println("Barre d'avancement :" + prog.getAvancement() + "%");
    }

}